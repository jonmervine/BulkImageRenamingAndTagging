package com.darkmage530.birat.clients

import arrow.core.Either
import arrow.core.continuations.either
import com.darkmage530.birat.DOROBOORU_URL
import com.darkmage530.birat.DorobooruError
import com.darkmage530.birat.clients.HttpClient.Companion.multipartHeader
import com.darkmage530.birat.clients.HttpClient.Companion.noAuthHeader
import com.darkmage530.birat.posts.PostFile
import com.darkmage530.birat.posts.PostRequest
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import java.io.File

// ?bump-login add as GET parameter to bump the token last logged in
//use authorization header example Authorization: Token dXNlcjE6dG9rZW4taXMtbW9yZS1zZWN1cmU=

/*
Every request must use Content-Type: application/json and Accept: application/json.
An exception to this rule are requests that upload files.
instead use multipart/form-data

//Send Token in  Authorization Header as `Token admin:uuid` where uuid is the generated user token not the basic "token" of username:password
 */

class PostClient(private val client: HttpClient, private val tokenClient: TokenClient) {
    private val url = "$DOROBOORU_URL/api/posts/"

    suspend fun get(id: Int): Either<DorobooruError, HttpResponse> =
        Either.catch {
            client.buildRequest("$DOROBOORU_URL/api/post/$id")
                .headers(noAuthHeader())
                .get()
                .execute()
        }.mapLeft {
            DorobooruError.UnexpectedThrownError(it.message, it).also { error -> println("Error, Get Post: $error") }
        }

    suspend fun postFile(
        postRequest: PostRequest,
        uploadFile: PostFile
    ): Either<DorobooruError, HttpResponse> {
        return either<DorobooruError, HttpResponse> {
            val token = tokenClient.getActiveToken().bind()
            Either.catch {
                client.buildRequest(url)
                    .post()
                    .headers(multipartHeader(token))
                    .body(MultiPartFormDataContent(
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
                    ))
                    .execute()
            }.mapLeft {
                DorobooruError.UnexpectedThrownError(it.message, it)
                    .also { error -> println("Error, Create Post: $error") }
            }.bind()
        }
    }

    //Delete doesn't page through posts just stops at one page
    suspend fun deleteAllPosts() {
        val token = tokenClient.getActiveToken().orNull()!!
        Either.catch {
            client.buildRequest(url)
                .headers(HttpClient.tokenAuthHeader(token))
                .get()
                .execute()
        }.mapLeft { DorobooruError.UnexpectedThrownError(it.message, it) }
            .map { response ->
                Either.catch {
                    Json.decodeFromString<JsonObject>(response.bodyAsText())["results"]
                        ?.jsonArray?.map { element ->
                            val id = element.jsonObject["id"]?.jsonPrimitive!!.int
                            val version = element.jsonObject["version"]!!.jsonPrimitive.int
                            println("Deleting post $id")
                            client.buildRequest("$DOROBOORU_URL/api/post/$id")
                                .headers(HttpClient.tokenAuthHeader(token))
                                .body(Json.encodeToString(Version(version)))
                                .delete()
                                .execute().also { response ->
                                    if (response.status.value != 200) println("Not Successful postDeletion $response")
                                }
                        }
                }.mapLeft {
                    DorobooruError.UnexpectedThrownError(it.message, it)
                        .also { error -> println("Error, Delete Post: $error") }
                }
            }
    }
}
