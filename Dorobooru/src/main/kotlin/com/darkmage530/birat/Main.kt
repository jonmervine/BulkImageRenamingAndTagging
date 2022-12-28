package com.darkmage530.birat;

import com.darkmage530.birat.clients.PostClient
import com.darkmage530.birat.clients.Token
import com.darkmage530.birat.clients.TokenClient
import com.darkmage530.birat.posts.Posts
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val tokenClient = TokenClient()
    val postClient = PostClient(tokenClient)

    Runtime.getRuntime().addShutdownHook(
        Thread {
            println("initiating shutdown sequence")
            postClient.shutdown()
            tokenClient.shutdown()
            println("shutdown complete")
        }
    )
    Posts(postClient).createPost()
//    tokenClient.getActiveToken()
    println("done")
//    val posts = Posts(postClient)
//    posts.createPost()
}

/*
Current state of things to do:

Have Post file take in the data to create a post
remove printlns

get over to application/common and continue there

 */