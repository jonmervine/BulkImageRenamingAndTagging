package com.darkmage530.birat.auth

import arrow.core.Either
import arrow.core.right
import com.darkmage530.birat.DOROBOORU_URL
import com.darkmage530.birat.DorobooruError
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonPrimitive
import java.time.Instant
import java.time.temporal.ChronoUnit

data class Token(val token: String, val note: String, val enabled: Boolean, val expirationTime: Instant)

class AuthConnectionClient {
        private var activeToken: Token? = null
    companion object {
        val client = HttpClient(CIO)  {
            install(ContentNegotiation) { json() }
//            install(ContentNegotiation) {(ContentType.MultiPart.FormData,  multiPartConverter())}
        }
    }
    fun shutdown() = client.close()

    @Serializable
    private data class TokenRequest(val enabled: Boolean, val note: String, val expirationTime: String)

    // ?bump-login add as GET parameter to bump the token last logged in
    //use authorization header example Authorization: Token dXNlcjE6dG9rZW4taXMtbW9yZS1zZWN1cmU=

    /*
    Every request must use Content-Type: application/json and Accept: application/json.
    An exception to this rule are requests that upload files.
    instead use multipart/form-data
     */

    suspend fun get(url: String): Either<DorobooruError, HttpResponse> =
        Either.catch {
            client.get(url) {
                headers {
                    append(HttpHeaders.Accept, "application/json")
                }
            }
        }.mapLeft { DorobooruError.UnexpectedThrownError(it.message, it) }


    suspend fun createUserToken() =
        client.post("$DOROBOORU_URL/api/user-token/birat") {
            headers {
                append(HttpHeaders.Authorization, "Basic ${"birat:birat".encodeBase64()}")
                append(HttpHeaders.Accept, "application/json")
                append(HttpHeaders.ContentType, "application/json")
            }

            setBody(Json.encodeToString(TokenRequest(true, "BIRAT Uploads", Instant.now().plus(6, ChronoUnit.HOURS).toString())))
        }.let { response ->
            val textbody = response.bodyAsText()
            println(textbody)
            val derp = Json.decodeFromString<JsonObject>(textbody)
            Token(
                derp["token"]!!.jsonPrimitive.content,
                derp["note"]!!.jsonPrimitive.content,
                derp["enabled"]!!.jsonPrimitive.boolean,
                Instant.parse(derp["expirationTime"]!!.jsonPrimitive.content))
        }
}