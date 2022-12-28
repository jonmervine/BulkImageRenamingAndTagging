package com.darkmage530.birat

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import java.io.File
import java.lang.RuntimeException

class Config() {
    companion object {
        fun getUsername() = getLocalProperty("username").toString()
        fun getPassword() = getLocalProperty("password").toString()
        fun getTokenNote() = getLocalProperty("tokenNote").toString()
        fun getExpirationTime() = getLocalProperty("expirationTime").toString().toLong()

        private fun getLocalProperty(key: String, file: String = "/local.properties"): Any {
            val properties = java.util.Properties()
            val inputStream = Companion::class.java.getResourceAsStream(file) ?: throw RuntimeException(DorobooruError.UnexpectedError("File or Property not found").toString())
                java.io.InputStreamReader(inputStream, Charsets.UTF_8).use { reader ->
                    properties.load(reader)
                }
                return properties.getProperty(key)
//            } else throw RuntimeException(DorobooruError.UnexpectedError("File or Property not found").toString())
        }
    }
}