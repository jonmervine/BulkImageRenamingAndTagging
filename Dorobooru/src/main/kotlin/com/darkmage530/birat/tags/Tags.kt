package com.darkmage530.birat.tags

import com.darkmage530.birat.clients.TagClient
import kotlinx.serialization.Serializable

enum class TagCategory { General, Artist, Character, Copyright, Meta }

@Serializable
data class TagRequest(val category: TagCategory, val version: Int)
data class TagResponse(
    val status: Int
)

class Tags(val tagClient: TagClient) {
    suspend fun updateTag(tagName: String, tagRequest: TagRequest): TagResponse {
        return tagClient.updateTag(tagName, tagRequest)
            .fold({ println("Tag.updateTag got an Error: $it"); TagResponse(400) }, { TagResponse(it.status.value) })
    }

}