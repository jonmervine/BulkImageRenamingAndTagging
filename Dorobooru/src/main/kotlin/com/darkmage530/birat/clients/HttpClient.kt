package com.darkmage530.birat.clients

import com.darkmage530.birat.Config
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*

class HttpClient {
    companion object {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) { json() }
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
            builder.apply { append(HttpHeaders.Authorization,
                "Token ${"${Config.getUsername()}:$token".encodeBase64()}") }
        }
    }

    fun shutdown() = client.close()
}