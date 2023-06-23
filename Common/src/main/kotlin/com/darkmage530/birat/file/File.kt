package com.darkmage530.birat.file

import arrow.core.raise.either
import arrow.core.raise.ensure

data class File(
    val fileReference: java.io.File,
    val path: String
) {
    companion object {
        operator fun invoke(file: java.io.File) = either {
            ensure(file.canRead()) { ImageError.FileWrapperError(file.path) }
            File(file, file.path)
        }
    }
}