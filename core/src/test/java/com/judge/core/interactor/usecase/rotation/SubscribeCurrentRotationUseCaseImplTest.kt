package com.judge.core.interactor.usecase.rotation

import com.google.common.truth.Truth.assertThat
import com.judge.core.domain.model.Competition
import com.judge.core.domain.result.Result
import com.judge.core.interactor.usecase.SubscribeCurrentTimeUseCase
import com.judge.core.interactor.usecase.competition.SubscribeCompetitionUseCase
import com.judge.core.interactor.usecase.competition.SubscribeCompetitionUseCaseImpl
import com.judge.core.util.tick.ClockFake
import com.judge.core.util.tick.TickHandler
import com.judge.core.util.tick.TickHandlerImpl
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class SubscribeCurrentRotationUseCaseImplTest {

    private val fakeCompetition = Competition(
        0, "Fake Comp", "Fake Location", 1000L,
        5, 5, 1, emptyList()
    )

    lateinit var subscribeCompetition: SubscribeCompetitionUseCase
    lateinit var clockFake: ClockFake
    lateinit var tickHandler: TickHandler
    lateinit var subscribeCurrentTime: SubscribeCurrentTimeUseCase
    lateinit var getRotation: GetRotationUseCase
    lateinit var subscribeCurrentRotation: SubscribeCurrentRotationUseCaseImpl

    lateinit var testScope: TestCoroutineScope

    @BeforeEach
    fun setup() {
        subscribeCompetition = mock()
        //subscribeCompetition = mock(SubscribeCompetitionUseCase::class.java)
        clockFake = ClockFake()
        tickHandler = TickHandlerImpl(clockFake)
        subscribeCurrentTime = SubscribeCurrentTimeUseCase(tickHandler)
        getRotation = GetRotationUseCase()
        subscribeCurrentRotation = SubscribeCurrentRotationUseCaseImpl(
            subscribeCompetition, subscribeCurrentTime, getRotation)
        testScope = TestCoroutineScope(CoroutineName("Test Scope"))
    }

    @Test
    fun `invoke method subscribe competition returns success result`() = testScope.runBlockingTest {
        whenever(subscribeCompetition(0, testScope)).thenReturn(
            MutableStateFlow(Result.Success(fakeCompetition)).asStateFlow()
        )
        val replay = mutableListOf<Result<Int>>()
        val rotation = subscribeCurrentRotation(0, testScope)
        testScope.launch {
            rotation.take(2).toList(replay)
        }
        testScope.advanceTimeBy(fakeCompetition.startTime)
        assertThat(replay).containsExactlyElementsIn(listOf(Result.Success(0), Result.Success(1)))
        testScope.cancel()
    }
}