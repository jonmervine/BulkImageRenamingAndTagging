package com.darkmage530.birat

sealed interface DorobooruError {

    data class UnexpectedThrownError(val message: String?, val throwable: Throwable): DorobooruError
    data class UnexpectedError(val message: String?): DorobooruError
}