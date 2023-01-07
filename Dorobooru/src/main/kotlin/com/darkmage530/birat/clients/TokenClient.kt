package com.darkmage530.birat.clients

import arrow.core.Either
import arrow.core.continuations.either
import com.darkmage530.birat.Config
import com.darkmage530.birat.DOROBOORU_URL
import com.darkmage530.birat.DorobooruError
import com.darkmage530.birat.clients.HttpClient.Companion.basicAuthHeader
import com.darkmage530.birat.clients.HttpClient.Companion.tokenAuthHeader
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import java.time.Instant
import java.time.temporal.ChronoUnit

data class Token(val token: String, val note: String, val enabled: Boolean, val expirationTime: Instant?) {
    companion object {
        operator fun invoke(jsonObject: JsonObject): Token =
            jsonObject["expirationTime"]?.jsonPrimitive?.content.let { expirationTime ->
                Token(
                    jsonObject["token"]?.jsonPrimitive!!.content,
                    jsonObject["note"]?.jsonPrimitive!!.content,
                    jsonObject["enabled"]?.jsonPrimitive!!.boolean,
                    if (expirationTime != "null") Instant.parse(expirationTime) else null
                )
            }
    }

    fun isExpired() = !enabled || expirationTime?.let { Instant.now().isAfter(expirationTime) } ?: false
}

class TokenClient(val client: HttpClient) {
    private var activeToken: Token? = null

    @Serializable
    private data class TokenRequest(val enabled: Boolean, val note: String, val expirationTime: String)

    suspend fun getActiveToken(): Either<DorobooruError, Token> =
        either {
            activeToken.let { token ->
                if (token?.isExpired() != false) {
                    createUserToken().bind().also { newUserToken ->
                        activeToken = newUserToken

                        coroutineScope {
                            launch { deleteExpiredTokens(newUserToken) }
                        }
                    }
                } else token
            }
        }

    private suspend fun createUserToken(): Either<DorobooruError, Token> =
        Either.catch {
            client.buildRequest("$DOROBOORU_URL/api/user-token/${Config.getUsername()}")
                .headers(basicAuthHeader(Config.getUsername(), Config.getPassword()))
                .body(
                    Json.encodeToString(
                        TokenRequest(
                            true,
                            Config.getTokenNote(),
                            Instant.now().plus(Config.getExpirationTime(), ChronoUnit.HOURS).toString()
                        )
                    )
                )
                .post()
                .execute()
                .let { response ->
                    val textBody = response.bodyAsText()
                    Token(Json.decodeFromString(textBody))
                }
        }.mapLeft {
            DorobooruError.UnexpectedThrownError(it.message, it)
                .also { error -> println("Error, Create Token: $error") }
        }


    private suspend fun deleteExpiredTokens(newUserToken: Token) {
        Either.catch {
            client.buildRequest("$DOROBOORU_URL/api/user-tokens/${Config.getUsername()}")
                .headers(tokenAuthHeader(newUserToken))
                .get()
                .execute()
        }.mapLeft { DorobooruError.UnexpectedThrownError(it.message, it) }
            .map { response ->
                Either.catch {
                    Json.decodeFromString<JsonObject>(response.bodyAsText())["results"]
                        ?.jsonArray?.map { element ->
                            Token(element.jsonObject)
                        }?.filter { token ->
                            token.isExpired()
                        }?.map { token ->
                            println("Deleting token $token")
                            client.buildRequest("$DOROBOORU_URL/api/user-token/${Config.getUsername()}/${token.token}")
                                .headers(tokenAuthHeader(newUserToken))
                                .body(Json.encodeToString(Version(1)))
                                .delete()
                                .execute()
                        }
                }.mapLeft {
                    DorobooruError.UnexpectedThrownError(it.message, it)
                        .also { error -> println("Error, Delete Token: $error") }
                }
            }
    }
}