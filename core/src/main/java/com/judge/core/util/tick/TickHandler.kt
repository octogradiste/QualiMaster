package com.judge.core.util.tick

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface TickHandler {

    fun subscribe(externalScope: CoroutineScope): StateFlow<Long>

}