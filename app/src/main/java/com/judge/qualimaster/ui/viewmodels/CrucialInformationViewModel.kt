package com.judge.qualimaster.ui.viewmodels

import androidx.lifecycle.*
import com.judge.core.data.Repository
import com.judge.core.interactor.CrucialInformationInteractor
import com.judge.core.interactor.usecase.*
import com.judge.core.interactor.usecase.athlete.AthleteBlockUseCase
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
import com.judge.qualimaster.util.ClockImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class CrucialInformationViewModel @Inject constructor(repository: Repository) : ViewModel() {

    private val competitionId = 1L
    private val tickHandler: TickHandler = TickHandlerImpl(viewModelScope, ClockImpl())
    private val athleteBlock = AthleteBlockUseCase()

    private val interactor = CrucialInformationInteractor(
            AthleteBoulderBlockUseCase(repository, athleteBlock),
            AthleteTransitBlockUseCase(repository, athleteBlock),
            SetStartTimeUseCase(repository),
            SubscribeCurrentTimeUseCase(viewModelScope, tickHandler),
            SubscribeCurrentRotationUseCase(
                SubscribeCompetitionUseCase(repository, viewModelScope),
                SubscribeCurrentTimeUseCase(viewModelScope, tickHandler),
                GetRotationUseCase(),
                viewModelScope,
            ),
            SubscribeCompetitionUseCase(repository, viewModelScope),
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