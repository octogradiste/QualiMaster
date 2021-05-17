package com.judge.qualimaster.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.judge.core.data.Repository
import com.judge.core.domain.Location
import com.judge.core.interactor.RotationHistoryInteractor
import com.judge.core.interactor.usecase.athlete.AthleteBoulderBlockUseCase
import com.judge.core.interactor.usecase.competition.RefreshCompetitionUseCase
import com.judge.core.interactor.usecase.competition.SubscribeCompetitionUseCase
import com.judge.core.presentation.presenter.RotationHistoryPresenter
import com.judge.core.util.tick.TickHandler
import com.judge.core.util.tick.TickHandlerImpl
import com.judge.qualimaster.data.BaseRepositoryImpl
import com.judge.qualimaster.util.ClockImpl
import kotlinx.coroutines.Dispatchers

class RotationHistoryViewModel : ViewModel() {
    private val competitionId = 0
    private val repository = Repository(BaseRepositoryImpl(), Location)
    private val tickHandler: TickHandler = TickHandlerImpl(viewModelScope, ClockImpl())

    private val interactor = RotationHistoryInteractor(
            AthleteBoulderBlockUseCase(repository),
            SubscribeCompetitionUseCase(repository),
            RefreshCompetitionUseCase(repository),
    )

    private val presenter = RotationHistoryPresenter(
            competitionId,
            interactor,
            Dispatchers.IO,
            viewModelScope,
    )

    val state = presenter.athleteListState.asLiveData()
    val rotationHistory = presenter.rotationHistory.asLiveData()

    suspend fun refresh() = presenter.refresh()
}