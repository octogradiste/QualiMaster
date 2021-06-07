package com.judge.core.util

interface TimeFormatter {

    val formatString: String

    fun format(time: Long): String

    fun parse(time: String): Long?

}