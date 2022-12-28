package com.darkmage530.birat.clients

import arrow.core.Either
import arrow.core.continuations.either
import com.darkmage530.birat.Config
import com.darkmage530.birat.DOROBOORU_URL
import com.darkmage530.birat.DorobooruError
import com.darkmage530.birat.clients.HttpClient.Companion.basicAuthHeader
import com.darkmage530.birat.clients.HttpClient.Companion.tokenAuthHeader
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
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

class TokenClient {
    private var activeToken: Token? = null

    @Serializable
    private data class TokenRequest(val enabled: Boolean, val note: String, val expirationTime: String)

    @Serializable
    private data class Version(val version: Int)

    suspend fun getActiveToken(): Either<DorobooruError, Token> =
        either {
            activeToken.let { token ->
                if (token?.isExpired() != false) {
                    println("is expired")
                    createUserToken().bind().also { newUserToken ->
                        coroutineScope {
                            launch { deleteExpiredTokens(newUserToken) }
                        }
                    }
                } else token
            }
        }

    companion object {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) { json() }
        }
    }

    fun shutdown() = HttpClient.client.close()

    private suspend fun createUserToken(): Either<DorobooruError, Token> =
        Either.catch {
            println("attempt create new token")
            client.post("$DOROBOORU_URL/api/user-token/${Config.getUsername()}") {
                headers {
                    appendAll(basicAuthHeader(Config.getUsername(), Config.getPassword()))
                }

                setBody(
                    Json.encodeToString(
                        TokenRequest(
                            true,
                            Config.getTokenNote(),
                            Instant.now().plus(Config.getExpirationTime(), ChronoUnit.HOURS).toString()
                        )
                    )
                )
            }.let { response ->
                println("got back response for create ${response.bodyAsText()}")
                val textBody = response.bodyAsText()
                Token(Json.decodeFromString(textBody))
            }
        }.mapLeft { println("got error"); DorobooruError.UnexpectedThrownError(it.message, it) }


    private suspend fun deleteExpiredTokens(newUserToken: Token) {
        println("deleting expired tokens")
        Either.catch {
            client.get("$DOROBOORU_URL/api/user-tokens/${Config.getUsername()}") {
                headers {
                    appendAll(tokenAuthHeader(newUserToken))
                }
            }
        }.mapLeft { DorobooruError.UnexpectedThrownError(it.message, it) }.map { response ->
            val results = Json.decodeFromString<JsonObject>(response.bodyAsText())
            println("retrieve all tokens")
            results["results"]?.jsonArray?.map { element ->
                Token(element.jsonObject)
            }?.filter { token ->
                token.isExpired()
            }?.map { token ->
                println("Deleting $token")
                client.delete("$DOROBOORU_URL/api/user-token/${Config.getUsername()}/${token.token}") {
                    headers {
                        appendAll(tokenAuthHeader(newUserToken))
                    }
                    setBody(Json.encodeToString(Version(1)))
                }
            }
        }
    }

}