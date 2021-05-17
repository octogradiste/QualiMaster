package com.judge.qualimaster.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.judge.core.data.Repository
import com.judge.core.domain.Location
import com.judge.core.interactor.AthleteLocationInteractor
import com.judge.core.interactor.usecase.*
import com.judge.core.interactor.usecase.athlete.AthleteBoulderBlockUseCase
import com.judge.core.interactor.usecase.athlete.AthleteIsolationBlockUseCase
import com.judge.core.interactor.usecase.athlete.AthleteMovingBlockUseCase
import com.judge.core.interactor.usecase.athlete.AthleteTransitBlockUseCase
import com.judge.core.interactor.usecase.competition.RefreshCompetitionUseCase
import com.judge.core.interactor.usecase.competition.SubscribeCompetitionUseCase
import com.judge.core.interactor.usecase.rotation.GetRotationUseCase
import com.judge.core.interactor.usecase.rotation.SubscribeCurrentRotationUseCase
import com.judge.core.presentation.presenter.AthleteLocationPresenter
import com.judge.core.util.tick.TickHandler
import com.judge.core.util.tick.TickHandlerImpl
import com.judge.qualimaster.data.BaseRepositoryImpl
import com.judge.qualimaster.util.ClockImpl
import kotlinx.coroutines.Dispatchers

class AthleteLocationViewModel : ViewModel() {
    private val competitionId = 0
    private val repository = Repository(BaseRepositoryImpl(), Location)
    private val tickHandler: TickHandler = TickHandlerImpl(viewModelScope, ClockImpl())

    private val interactor = AthleteLocationInteractor(
            AthleteBoulderBlockUseCase(repository),
            AthleteTransitBlockUseCase(repository),
            AthleteMovingBlockUseCase(repository),
            AthleteIsolationBlockUseCase(repository),
            SubscribeCurrentRotationUseCase(
                SubscribeCompetitionUseCase(repository),
                SubscribeCurrentTimeUseCase(viewModelScope, tickHandler),
                GetRotationUseCase(),
                viewModelScope,
            ),
            SubscribeCompetitionUseCase(repository),
            RefreshCompetitionUseCase(repository),
    )

    private val presenter = AthleteLocationPresenter(
            competitionId,
            interactor,
            Dispatchers.IO,
            viewModelScope,
    )

    val state = presenter.athleteListState.asLiveData()
    val athleteLocation = presenter.athleteLocation.asLiveData()

    suspend fun refresh() = presenter.refresh()
}