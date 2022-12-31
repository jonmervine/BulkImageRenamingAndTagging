package com.darkmage530.birat.clients

import arrow.core.Either
import arrow.core.continuations.either
import com.darkmage530.birat.DOROBOORU_URL
import com.darkmage530.birat.DorobooruError
import com.darkmage530.birat.tags.TagRequest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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

    //TODO Delete Tag
}