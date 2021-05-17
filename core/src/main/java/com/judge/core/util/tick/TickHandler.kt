package com.judge.core.util.tick

import kotlinx.coroutines.flow.StateFlow

interface TickHandler {

    fun subscribe(): StateFlow<Long>

}