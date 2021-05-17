package com.judge.core.domain.result

sealed class Response {
    data class Success(val info: String) : Response()
    data class Error(val msg: String, val cause: Exception? = null) : Response()
}
