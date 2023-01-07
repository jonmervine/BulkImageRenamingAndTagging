package com.darkmage530.birat.clients

import arrow.core.Either
import arrow.core.continuations.either
import com.darkmage530.birat.DOROBOORU_URL
import com.darkmage530.birat.DorobooruError
import com.darkmage530.birat.tags.TagRequest
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*

class TagClient(private val client: HttpClient, private val tokenClient: TokenClient) {
    /*
    We'll need to take in new tags and make sure to assign them at the end to the right categories
     */
    private val url = "$DOROBOORU_URL/api/tag/"

    //TODO Update Tag Category on Tag
    suspend fun updateTag(
        tagName: String,
        tagRequest: TagRequest
    ): Either<DorobooruError, HttpResponse> {
        return either<DorobooruError, HttpResponse> {
            val token = tokenClient.getActiveToken().bind()
            Either.catch {
                client.buildRequest(url + tagName)
                    .put()
                    .headers(HttpClient.tokenAuthHeader(token))
                    .body(Json.encodeToString(tagRequest))
                    .execute()
            }.mapLeft {
                DorobooruError.UnexpectedThrownError(it.message, it)
                    .also { error -> println("Error, Create Post: $error") }
            }.bind()
        }
    }

    //Delete doesn't page through posts just stops at one page
    suspend fun deleteAllTags() {
        val token = tokenClient.getActiveToken().orNull()!!
        Either.catch {
            client.buildRequest("$DOROBOORU_URL/api/tags/")
                .headers(HttpClient.tokenAuthHeader(token))
                .get()
                .execute()
        }.mapLeft { DorobooruError.UnexpectedThrownError(it.message, it) }
            .map { response ->
                Either.catch {
                    Json.decodeFromString<JsonObject>(response.bodyAsText())["results"]
                        ?.jsonArray?.map { element ->
                            val tagName = element.jsonObject["names"]?.jsonArray!!.first().jsonPrimitive.content
                            val version = element.jsonObject["version"]!!.jsonPrimitive.int
                            println("Deleting tag $tagName")
                            client.buildRequest("$DOROBOORU_URL/api/tag/$tagName")
                                .headers(HttpClient.tokenAuthHeader(token))
                                .body(Json.encodeToString(Version(version)))
                                .delete()
                                .execute().also { response ->
                                    if (response.status.value != 200) println("Not Successful TagDeletion $response")
                                }
                        }
                }.mapLeft {
                    DorobooruError.UnexpectedThrownError(it.message, it)
                        .also { error -> println("Error, Delete Tag: $error") }
                }
            }
    }
}