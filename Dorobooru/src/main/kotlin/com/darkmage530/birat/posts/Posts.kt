package com.darkmage530.birat.posts

import com.darkmage530.birat.DOROBOORU_URL
import com.darkmage530.birat.auth.AuthConnectionClient
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.ContentDisposition.Companion.File
import io.ktor.util.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.checkerframework.checker.guieffect.qual.SafeType
import java.io.File
import java.time.Instant
import java.time.temporal.ChronoUnit

class Posts(val booruClient: AuthConnectionClient) {
    suspend fun getPost() {
        booruClient.get("$DOROBOORU_URL/api/post/11").map { println(it.bodyAsText()) }
    }

    /*
Create post
{
"tags":      [<tag1>, <tag2>, <tag3>],
"safety":    <safety>,
"source":    <source>,                    // optional
"relations": [<post1>, <post2>, <post3>], // optional
"notes":     [<note1>, <note2>, <note3>], // optional
"flags":     [<flag1>, <flag2>],          // optional
"anonymous": <anonymous>                  // optional
}
 */

    @Serializable
    private enum class Safety { safe, sketchy, unsafe }

    @Serializable
    private data class PostRequest(
        val tags: List<String>,
        val safety: Safety,
        val source: String
    )

    suspend fun createPost() {
        val client = HttpClient(CIO)
        val token = booruClient.createUserToken()
        val response: HttpResponse = client.post("$DOROBOORU_URL/api/posts/") {
            headers {
                append(HttpHeaders.ContentType, "multipart/form-data")
                append(HttpHeaders.UserAgent, "ktor client")
                append(HttpHeaders.Authorization, "Token ${"birat:${token.token}".encodeBase64()}")
                append(HttpHeaders.Accept, "application/json")
            }
            setBody(MultiPartFormDataContent(
                formData {
                    append("metadata", Json.encodeToString(
                        PostRequest(
                            listOf("aketa_mikoto", "idolmaster"),
                            Safety.sketchy,
                            "none sorry"
                        )
                    ))
                    append("content",
                        File("D:\\Downloads\\__aketa_mikoto_idolmaster_and_1_more_drawn_by_ame_uten_cancel__c62fc980ee96a90caf119219ad6b597f.jpg")
                            .readBytes(),
                        Headers.build {
                            append(HttpHeaders.ContentType, "image/jpg")
                            append(HttpHeaders.ContentDisposition, "filename=\"aketa_mikoto.jpg\"")
                        }
                    )

                }
            )
            )
        }


        //Send Token in  Authorization Header as `Token admin:uuid` where uuid is the generated user token not the basic "token" of username:password
        println(response.status)
        println(response.bodyAsText())
        client.close()
    }


}