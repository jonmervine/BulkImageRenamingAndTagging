package com.darkmage530.birat.auth

import io.kotest.core.spec.style.StringSpec
import java.time.Instant
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class AuthConfigTest : StringSpec({

    "test serialization" {

        var testmap: Map<String, Any> = mapOf("note" to "BIRAT Uploads")
        Json.encodeToString(testmap)
        testmap = mapOf("note" to "BIRAT Uploads", "expirationTime" to Instant.now().toString())
        Json.encodeToString(testmap)
        testmap = mapOf("enabled" to true, "note" to "BIRAT Uploads", "expirationTime" to Instant.now().toString())
        Json.encodeToString(testmap)
    }
})