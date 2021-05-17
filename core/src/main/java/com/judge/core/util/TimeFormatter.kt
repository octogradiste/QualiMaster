package com.judge.core.util

interface TimeFormatter {

    fun format(time: Long): String

    fun parse(time: String): Long?

}