package com.judge.core.domain.result

fun Response.Error.asResult() = Result.Error(this.msg, this.cause)

fun Result.Error.asResponse() = Response.Error(this.msg, this.cause)