package com.darkmage530.birat.auth

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import java.time.Instant

class AuthConfig {
    val baseUrl = "http://192.168.1.162:8081"
    // ?bump-login add as GET parameter to bump the token last logged in
    //use authorization header example Authorization: Token dXNlcjE6dG9rZW4taXMtbW9yZS1zZWN1cmU=

    /*
    Every request must use Content-Type: application/json and Accept: application/json.
    An exception to this rule are requests that upload files.
    instead use multipart/form-data
     */


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
    suspend fun getPost() {
        val client = HttpClient(CIO)
        val response: HttpResponse = client.get("$baseUrl/api/post/11") {
            headers {
                append(HttpHeaders.Accept, "application/json")
            }
        }
        println(response.status)
        println(response.bodyAsText())
        client.close()
    }

    @Serializable
    data class TokenRequest(val enabled: Boolean, val note: String, val expirationTime: String)

    suspend fun createUserToken() {
        //POST /user-token/<user_name>
        /*
        {
    "enabled":        <enabled>,        // optional
    "note":           <note>,           // optional
    "expirationTime": <expiration-time> // optional
} //1 week 604800000
         */

        val client = HttpClient(CIO)  {
            install(ContentNegotiation) { json() }
        }

        val response: HttpResponse = client.post("$baseUrl/user-token/birat") {
            headers {
                append(HttpHeaders.Authorization, "birat:birat")
                append(HttpHeaders.Accept, "application/json")
                append(HttpHeaders.ContentType, "application/json")
                append(HttpHeaders.UserAgent, "Birat")
            }
//            accept(ContentType.Application.Json)
//            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(mapOf("enabled" to true, "note" to "BIRAT Uploads", "expirationTime" to Instant.now().toString())))
        }
        println(response.bodyAsText())
    }

    suspend fun createPost() {
        val client = HttpClient(CIO)
        val response: HttpResponse = client.post("$baseUrl/api/posts/") {
            headers {
                append(HttpHeaders.Accept, "multipart/form-data")
                append(HttpHeaders.UserAgent, "ktor client")
                append(HttpHeaders.Authorization, "admin:admin")
            }
        }
        println(response.status)
        println(response.bodyAsText())
        client.close()
    }
}