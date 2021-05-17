package com.judge.qualimaster.ui.viewmodels

import androidx.lifecycle.*
import com.judge.core.data.Repository
import com.judge.core.domain.Location
import com.judge.core.interactor.CrucialInformationInteractor
import com.judge.core.interactor.usecase.*
import com.judge.core.interactor.usecase.athlete.AthleteBoulderBlockUseCase
import com.judge.core.interactor.usecase.athlete.AthleteTransitBlockUseCase
import com.judge.core.interactor.usecase.competition.RefreshCompetitionUseCase
import com.judge.core.interactor.usecase.competition.SetStartTimeUseCase
import com.judge.core.interactor.usecase.competition.SubscribeCompetitionUseCase
import com.judge.core.interactor.usecase.rotation.GetRotationUseCase
import com.judge.core.interactor.usecase.rotation.SubscribeCurrentRotationUseCase
import com.judge.core.presentation.presenter.CrucialInformationPresenter
import com.judge.core.util.tick.TickHandler
import com.judge.core.util.tick.TickHandlerImpl
import com.judge.qualimaster.data.BaseRepositoryImpl
import com.judge.qualimaster.util.ClockImpl
import kotlinx.coroutines.Dispatchers

class CrucialInformationViewModel : ViewModel() {

    private val competitionId = 0
    private val repository = Repository(BaseRepositoryImpl(), Location)
    private val tickHandler: TickHandler = TickHandlerImpl(viewModelScope, ClockImpl())

    private val interactor = CrucialInformationInteractor(
            AthleteBoulderBlockUseCase(repository),
            AthleteTransitBlockUseCase(repository),
            SetStartTimeUseCase(repository),
            SubscribeCurrentTimeUseCase(viewModelScope, tickHandler),
            SubscribeCurrentRotationUseCase(
                SubscribeCompetitionUseCase(repository),
                SubscribeCurrentTimeUseCase(viewModelScope, tickHandler),
                GetRotationUseCase(),
                viewModelScope,
            ),
            SubscribeCompetitionUseCase(repository),
            RefreshCompetitionUseCase(repository),
       )

    private val presenter = CrucialInformationPresenter(
            competitionId,
            interactor,
            Dispatchers.IO,
            viewModelScope,
    )

    val state = presenter.athleteListState.asLiveData()
    val startTime = presenter.startTime.asLiveData()
    val currentTime = presenter.currentTime.asLiveData()
    val currentRotation = presenter.currentRotation.asLiveData()
    val crucialInformation = presenter.crucialInformation.asLiveData()

    suspend fun setStartTime(time: Long) = presenter.setStartTime(time)
    suspend fun refresh() = presenter.refresh()

}