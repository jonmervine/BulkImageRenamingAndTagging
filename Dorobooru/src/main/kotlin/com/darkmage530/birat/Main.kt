package com.darkmage530.birat;

import com.darkmage530.birat.auth.AuthConfig
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    println("Hello world!")
    AuthConfig().createUserToken()
}