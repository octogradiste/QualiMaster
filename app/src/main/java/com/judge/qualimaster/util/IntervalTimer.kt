package com.judge.qualimaster.util

import android.os.Handler
import android.os.Looper

typealias IntervalTimerListener = (Long) -> Unit

abstract class IntervalTimer() {
    abstract fun setIntervalTimerListener(listener: IntervalTimerListener)
}

class SimpleIntervalTimer(private val intervalInMillis: Long = 200) : IntervalTimer() {

    private val handler = Handler(Looper.getMainLooper())

    override fun setIntervalTimerListener(listener: IntervalTimerListener) {
        handler.postDelayed(
                object : Runnable {
                    override fun run() {
                        listener(System.currentTimeMillis())
                        handler.postDelayed(this, intervalInMillis)
                    }
                },
                intervalInMillis
        )
    }
}