package com.darkmage530.birat.file

import arrow.core.raise.either

data class Image(val file: File,
                 val md5: Md5,
                 val dimensions: Dimensions) {

    companion object {
        operator fun invoke(file: java.io.File) = either {
            val fileWrapper = File(file).bind()
            val a = getMd5(fileWrapper).bind()
            val b = getDimensions(fileWrapper).bind()
            Image(fileWrapper, a, b)
        }.mapLeft {
            it
        }
    }
}

//    fun getTags(): List<String>
//    fun setLocation(location: com.darkmage530.birat.file.File)
//    fun getLocation(): com.darkmage530.birat.file.File
//    fun getRating(): String