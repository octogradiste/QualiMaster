package com.judge.core.util.tick

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class TickHandlerImplTest {

    private val intervalMs = 50L

    lateinit var clockFake: ClockFake
    lateinit var tickHandler: TickHandler
    lateinit var testScope: TestCoroutineScope

    @BeforeEach
    fun setup() {
        clockFake = ClockFake()
        clockFake.incrementBy = intervalMs

        tickHandler = TickHandlerImpl(clockFake, intervalMs)
        testScope = TestCoroutineScope()

    }

    @Test
    fun `subscribe method`(){
        clockFake.currentTime = 113L
        val tick = tickHandler.subscribe(testScope)
        assertThat(tick.value).isEqualTo(113L)
    }

    @Test
    fun `subscribe method after one interval`() = runBlockingTest {
        val replay = mutableListOf<Long>()
        val tick = tickHandler.subscribe(testScope)
        val job = launch { tick.toList(replay) }
        testScope.advanceTimeBy(3*intervalMs)
        assertThat(replay).containsExactlyElementsIn(listOf(0, intervalMs, 2*intervalMs, 3*intervalMs)).inOrder()
        job.cancel()
    }

}