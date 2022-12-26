package com.darkmage530.birat;

import com.darkmage530.birat.auth.AuthConnectionClient
import com.darkmage530.birat.posts.Posts
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val authConnectionClient = AuthConnectionClient()

    Runtime.getRuntime().addShutdownHook(
        Thread {
            println("initiating shutdown sequence")
            authConnectionClient.shutdown()
            println("shutdown complete")
        }
    )

    val posts = Posts(authConnectionClient)
    posts.createPost()
}