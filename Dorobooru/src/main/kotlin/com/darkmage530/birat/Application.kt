package com.darkmage530.birat;

import com.darkmage530.birat.clients.HttpClient
import com.darkmage530.birat.clients.PostClient
import com.darkmage530.birat.clients.TagClient
import com.darkmage530.birat.clients.TokenClient
import com.darkmage530.birat.posts.PostFile
import com.darkmage530.birat.posts.PostRequest
import com.darkmage530.birat.posts.Posts
import com.darkmage530.birat.posts.Safety
import com.darkmage530.birat.tags.TagRequest
import com.darkmage530.birat.tags.Tags
import kotlinx.coroutines.runBlocking

class DorobooruService {
    companion object {
        private val client = HttpClient()
        private val tokenClient = TokenClient(client)
        private val postClient = PostClient(client, tokenClient)
        private val tagClient = TagClient(client, tokenClient)
        private val posts = Posts(postClient)
        private val tags = Tags(tagClient)
    }

    fun createPost(postRequest: PostRequest, postFile: PostFile) = runBlocking {
        return@runBlocking posts.createPost(postRequest, postFile)
    }

    fun updateTag(tag: String, tagRequest: TagRequest) = runBlocking {
        return@runBlocking tags.updateTag(tag, tagRequest)
    }

    fun clearEverything() = runBlocking {
        postClient.deleteAllPosts()
        tagClient.deleteAllTags()
    }
}

fun main() {
//    DorobooruService().updateTag("touhou", TagRequest(TagCategory.Copyright, 1))
//    DorobooruService().clearEverything()

    //    val tokenClient = TokenClient()
//    val postClient = PostClient(tokenClient)

//    Runtime.getRuntime().addShutdownHook(
//        Thread {
//            println("initiating shutdown sequence")
//            postClient.shutdown()
//            tokenClient.shutdown()
//            println("shutdown complete")
//        }
//    )
//    DorobooruService().createPost(
//        PostRequest(
//        listOf("hibike_euphonium", "1girl", "kumiko_oumae", "fluff"),
//        Safety.sketchy,
//        "https://www.pixiv.net/en/artworks/103589401"
//    ), PostFile("D:\\Downloads\\102890995_p0.png")
//    )
//    println("first done")
//
//    DorobooruService().createPost(
//        PostRequest(
//            listOf("hololive", "1girl", "ayane", "bouquet", "horns"),
//            Safety.safe,
//            "https://www.pixiv.net/en/artworks/103589401"
//        ), PostFile("D:\\Downloads\\103602570_p1.jpg")
//    )
//    println("second done")
//

////    tokenClient.getActiveToken()
    println("complete")
////    val posts = Posts(postClient)
////    posts.createPost()
}

/*
Current state of things to do:

Check szuruchrome to see how to get the categories added together
    https://github.com/neobooru/SzuruChrome/blob/b899d2b7942d6833193a7560d239cd10c0fe6356/src/scripts/background.ts
do that for handling tags or flesh out the TagClient

content type application/json
    https://danbooru.donmai.us/posts?md5=c62fc980ee96a90caf119219ad6b597f
tag_string_general
tag_string_character
tag_string_copyright
tag_string_artist
tag_string_meta
rating
    g = general
    s = sensitive
    q = questionable
    e = explicit

get over to application/common and continue there

 */