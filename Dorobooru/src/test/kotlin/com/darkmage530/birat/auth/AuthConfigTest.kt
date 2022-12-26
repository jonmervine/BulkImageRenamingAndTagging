package com.darkmage530.birat.auth

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.ktor.util.*
import io.mockk.core.ValueClassSupport.boxedValue
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import java.time.Instant
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonObject

class AuthConfigTest : StringSpec({

    @Serializable
    data class TokenRequest(val enabled: Boolean, val note: String, val expirationTime: String)

    "test serialization" {

        var testmap: Map<String, String> = mapOf("note" to "BIRAT Uploads")
        var output = Json.encodeToString(testmap)
        println(output)
        testmap = mapOf("note" to "BIRAT Uploads", "expirationTime" to Instant.now().toString())
        output = Json.encodeToString(testmap)
        println(output)
        testmap = mapOf("enabled" to "true", "note" to "BIRAT Uploads", "expirationTime" to Instant.now().toString())
        output = Json.encodeToString(testmap)
        println(output)

        val tokenRequest = TokenRequest(true, "BIRAT Uploads", Instant.now().toString())
        output = Json.encodeToString(tokenRequest)
        println(output)
    }

    "test the base64 token" {
        "user1:token-is-more-secure".encodeBase64()
            .shouldBe("dXNlcjE6dG9rZW4taXMtbW9yZS1zZWN1cmU=")

        println("biart:birat".encodeBase64())
        println("admin:admin".encodeBase64())

    }

    "test deserialization" {
        val text ="""{
  "user": {
    "name": "birat",
    "avatarUrl": "https://gravatar.com/avatar/290b113adc640c6de4c8af474aefbd67?d=retro&s=300"
  },
  "token": "5152dd9d-14fd-4c75-b51e-a6cf172be03c",
  "note": "BIRAT Uploads",
  "enabled": true,
  "expirationTime": "2022-12-23T10:39:42.025000Z",
  "creationTime": "2022-12-23T04:39:41.914969Z",
  "lastEditTime": "2022-12-23T04:39:41.915138Z",
  "lastUsageTime": "2022-12-23T04:39:41.914978Z",
  "version": 1
}"""
        val derp = Json.decodeFromString<JsonObject>(text)["expirationTime"]
        Instant.parse(derp.toString())
        Instant.parse("2022-12-23T10:39:42.025000Z")
    }
})