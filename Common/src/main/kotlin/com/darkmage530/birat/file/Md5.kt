package com.darkmage530.birat.file

import arrow.core.Either
import org.apache.commons.codec.digest.DigestUtils
import java.io.FileInputStream

fun getMd5(image: File) =
    Either.catch {
        FileInputStream(image.fileReference).use { fis ->
            DigestUtils.md5Hex(fis)
        }
    }.mapLeft {
        ImageError.Md5Error(it)
    }


typealias Md5 = String