package com.darkmage530.birat.clients

import com.darkmage530.birat.Config
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*

class HttpClient {
    companion object {
        val test: String = "fdajkfds"
        val client = HttpClient(CIO) {
            install(ContentNegotiation) { json() }
        }.also {
            Runtime.getRuntime().addShutdownHook(
                Thread {
                    println("Shutting down HttpClient")
                    it.close()
                })
        }

        val noAuthHeader: () -> HeadersBuilder = {
            HeadersBuilder().let { builder ->
                userAgentAcceptJson(builder)
                contentTypeJson(builder)
            }
        }

        val basicAuthHeader: (String, String) -> HeadersBuilder = { username: String, password: String ->
            HeadersBuilder().let { builder ->
                contentTypeJson(builder)
                builder.append(HttpHeaders.Authorization, "Basic ${"$username:$password".encodeBase64()}")
                userAgentAcceptJson(builder)
            }
        }

        val tokenAuthHeader: (Token) -> HeadersBuilder = { token: Token ->
            HeadersBuilder().let { builder ->
                contentTypeJson(builder)
                tokenAuth(builder, token.token)
                userAgentAcceptJson(builder)
            }
        }

        val multipartHeader: (Token) -> HeadersBuilder = { token: Token ->
            HeadersBuilder().let { builder ->
                builder.append(HttpHeaders.ContentType, "multipart/form-data")
                tokenAuth(builder, token.token)
                userAgentAcceptJson(builder)
            }
        }

        private val userAgentAcceptJson: (HeadersBuilder) -> HeadersBuilder = { builder ->
            builder.apply {
                append(HttpHeaders.Accept, "application/json")
                append(HttpHeaders.UserAgent, "ktor client")
            }
        }
        private val contentTypeJson: (HeadersBuilder) -> HeadersBuilder = { builder ->
            builder.apply { append(HttpHeaders.ContentType, "application/json") }
        }
        private val tokenAuth: (HeadersBuilder, String) -> HeadersBuilder = { builder, token ->
            builder.apply {
                append(
                    HttpHeaders.Authorization,
                    "Token ${"${Config.getUsername()}:$token".encodeBase64()}"
                )
            }
        }
    }

    fun buildRequest(url: String) =
        HttpRequest(HttpRequestBuilder().apply { url(url) }, client)
}

data class HttpResponse(val response: io.ktor.client.statement.HttpResponse) {
    val status = response.status
    suspend fun bodyAsText(): String {
        return response.bodyAsText()
    }
}

class HttpRequest(private val requestBuilder: HttpRequestBuilder, private val client: HttpClient) {

    fun headers(headers: HeadersBuilder): HttpRequest =
        this.apply {
            requestBuilder.headers { appendAll(headers) }
        }


    fun body(body: Any): HttpRequest =
        this.apply {
            requestBuilder.setBody(body)
        }

    fun post(): HttpRequest = this.apply { requestBuilder.method = HttpMethod.Post }

    fun get(): HttpRequest = this.apply { requestBuilder.method = HttpMethod.Get }

    fun put(): HttpRequest = this.apply { requestBuilder.method = HttpMethod.Put }

    fun delete(): HttpRequest = this.apply { requestBuilder.method = HttpMethod.Delete }

    suspend fun execute(): HttpResponse = HttpResponse(client.request(requestBuilder))
}

