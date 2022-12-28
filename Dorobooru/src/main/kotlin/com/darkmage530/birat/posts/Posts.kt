package com.darkmage530.birat.posts

import com.darkmage530.birat.DOROBOORU_URL
import com.darkmage530.birat.clients.PostClient
import io.ktor.client.statement.*
import kotlinx.serialization.Serializable

@Serializable
enum class Safety { safe, sketchy, unsafe }

@Serializable
data class PostRequest(
    val tags: List<String>,
    val safety: Safety,
    val source: String
)

data class PostFile(
    val path: String,
    val contentType: String = "image/jpg",
    val contentDisposition: String = "filename=\"aketa_mikoto.jpg\""
)

class Posts(val postClient: PostClient) {
    suspend fun getPost() {
        postClient.get("$DOROBOORU_URL/api/post/11").map { println(it.bodyAsText()) }
    }

    suspend fun createPost() {
        postClient.postFile(
            "$DOROBOORU_URL/api/posts/", PostRequest(
                listOf("1girl", ":o", "bangs", "bare_arms", "bare_legs"),
                Safety.sketchy,
                "https://www.pixiv.net/en/artworks/75140776"
            ), PostFile("D:\\Downloads\\75140776_p0.jpg")
        ).map { response ->
            println(response.status)
            println(response.bodyAsText())
        }
    }
}