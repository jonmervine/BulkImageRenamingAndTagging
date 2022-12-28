package com.darkmage530.birat.clients

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.flatten
import com.darkmage530.birat.DorobooruError
import com.darkmage530.birat.clients.HttpClient.Companion.multipartHeader
import com.darkmage530.birat.clients.HttpClient.Companion.basicAuthHeader
import com.darkmage530.birat.clients.HttpClient.Companion.noAuthHeader
import com.darkmage530.birat.posts.PostFile
import com.darkmage530.birat.posts.PostRequest
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import kotlinx.serialization.encodeToString
import java.io.File

// ?bump-login add as GET parameter to bump the token last logged in
//use authorization header example Authorization: Token dXNlcjE6dG9rZW4taXMtbW9yZS1zZWN1cmU=

/*
Every request must use Content-Type: application/json and Accept: application/json.
An exception to this rule are requests that upload files.
instead use multipart/form-data

//Send Token in  Authorization Header as `Token admin:uuid` where uuid is the generated user token not the basic "token" of username:password
 */

class PostClient(private val tokenClient: TokenClient) {

    companion object {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) { json() }
        }
    }
    fun shutdown() = HttpClient.client.close()

    suspend fun get(url: String): Either<DorobooruError, HttpResponse> =
        Either.catch {
            client.get(url) {
                headers {
                    appendAll(noAuthHeader())
                }
            }
        }.mapLeft { DorobooruError.UnexpectedThrownError(it.message, it) }

    suspend fun postFile(
        url: String,
        postRequest: PostRequest,
        uploadFile: PostFile
    ): Either<DorobooruError, HttpResponse> = either {
        println("get active token")
        val token = tokenClient.getActiveToken().bind()
        println("got new token: $token")
        Either.catch {
            client.post(url) {
                headers {
                    appendAll(multipartHeader(token))
                }
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("metadata", Json.encodeToString(postRequest))
                            append("content",
                                File(uploadFile.path).readBytes(),
                                Headers.build {
                                    append(HttpHeaders.ContentType, uploadFile.contentType)
                                    append(HttpHeaders.ContentDisposition, uploadFile.contentDisposition)
                                }
                            )
                        }
                    )
                )
            }.also { println(it.bodyAsText()) }

        }.mapLeft { DorobooruError.UnexpectedThrownError(it.message, it) }
    }.flatten()
}
