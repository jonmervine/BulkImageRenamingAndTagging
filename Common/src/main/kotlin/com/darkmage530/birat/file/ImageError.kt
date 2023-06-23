package com.darkmage530.birat.file

import arrow.core.Nel

sealed interface ImageError {
    data class Md5Error(val ex: Throwable) : ImageError
    data class ImageDimensionError(val ex: Throwable) : ImageError
    data class FileWrapperError(val path: String) : ImageError
}

data class CollectedErrors(val errors: Nel<ImageError>)