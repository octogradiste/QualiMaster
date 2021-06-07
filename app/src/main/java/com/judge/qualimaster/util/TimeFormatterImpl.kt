package com.judge.qualimaster.util

import com.judge.core.util.TimeFormatter
import java.text.SimpleDateFormat

class TimeFormatterImpl(override val formatString: String) : TimeFormatter {

    private val formatter = SimpleDateFormat(formatString)

    override fun format(time: Long): String = formatter.format(time)

    override fun parse(time: String): Long? = formatter.parse(time)?.time

}