package com.judge.core.util.tick

import com.judge.core.util.Clock

class ClockFake() : Clock {

    var currentTime = 0L
    var incrementBy = 50L

    override fun currentTimeMs(): Long {
        currentTime += incrementBy
        return currentTime - incrementBy
    }
}