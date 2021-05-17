package com.judge.qualimaster.fakes

import com.judge.qualimaster.util.IntervalTimer
import com.judge.qualimaster.util.IntervalTimerListener

class FakeIntervalTimer : IntervalTimer() {

    private var listener: IntervalTimerListener = {}

    override fun setIntervalTimerListener(listener: IntervalTimerListener) {
        this.listener = listener
    }

    fun callIntervalTimerListener(timeInMillis: Long) {
        listener(timeInMillis)
    }
}