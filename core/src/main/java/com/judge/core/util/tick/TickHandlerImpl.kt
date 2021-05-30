package com.judge.core.util.tick

import com.judge.core.util.Clock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TickHandlerImpl (
        private val clock: Clock,
        private val tickIntervalMs: Long = 200
) : TickHandler {

    private val _tick = MutableStateFlow(0L)

    override fun subscribe(externalScope: CoroutineScope): StateFlow<Long> {
        externalScope.launch {
            while(true) {
                _tick.emit(clock.currentTimeMs())
                delay(tickIntervalMs)
            }
        }
        return _tick.asStateFlow()
    }
}
