package com.judge.core.domain.result

sealed class Result<out T: Any> {
    data class Success<out T: Any>(val value: T): Result<T>()
    data class Error(val msg: String, val cause: Exception? = null): Result<Nothing>()
}
