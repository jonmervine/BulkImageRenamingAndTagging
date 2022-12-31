package com.darkmage530.birat.posts

import com.darkmage530.birat.clients.PostClient
import kotlinx.serialization.Serializable

@Serializable
enum class Safety {
    safe, sketchy, unsafe;

    companion object {
        fun fromDanbooru(rating: String): Safety {
            return when (rating) {
                "g" -> safe
                "s" -> sketchy
                "q", "e" -> unsafe
                else -> unsafe
            }
        }
    }
}

@Serializable
data class PostRequest(
    val tags: List<String>,
    val safety: Safety,
    val source: String
)

data class PostFile(
    val path: String,
    val contentType: String = "image/jpg",
    val contentDisposition: String = "filename=\"filename.jpg\""
) {
    companion object {
        private val contentDispositionTemplate = "filename=\"filename."
        private val contentTypeTemplate = "image/"
        fun invoke(filepath: String) =
            filepath.substring(filepath.lastIndexOf("."), filepath.length).let { extension ->
                PostFile(filepath, contentTypeTemplate + extension, contentDispositionTemplate + extension)
            }
    }
}

data class PostResponse(
    val status: Int
)

class Posts(val postClient: PostClient) {
    suspend fun getPost(id: Int) {
        postClient.get(id)
    }

    suspend fun createPost(postRequest: PostRequest, postFile: PostFile): PostResponse {
        return postClient.postFile(postRequest, postFile)
            .fold({ println("Posts.createPost got an Error: $it"); PostResponse(400) },
                { PostResponse(it.status.value) })
    }
}