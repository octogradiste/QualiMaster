package com.judge.qualimaster.util

import com.judge.core.util.Clock

class ClockImpl : Clock {
    override fun currentTimeMs() = System.currentTimeMillis()
}