package com.judge.qualimaster.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.judge.core.data.Repository
import com.judge.core.interactor.CrucialInformationInteractor
import com.judge.core.interactor.usecase.SubscribeCurrentTimeUseCase
import com.judge.core.interactor.usecase.athlete.AthleteBlockUseCase
import com.judge.core.interactor.usecase.athlete.AthleteBoulderBlockUseCase
import com.judge.core.interactor.usecase.athlete.AthleteTransitBlockUseCase
import com.judge.core.interactor.usecase.competition.RefreshCompetitionUseCase
import com.judge.core.interactor.usecase.competition.SetStartTimeUseCase
import com.judge.core.interactor.usecase.competition.SubscribeCompetitionUseCaseImpl
import com.judge.core.interactor.usecase.rotation.GetRotationUseCase
import com.judge.core.interactor.usecase.rotation.SubscribeCurrentRotationUseCaseImpl
import com.judge.core.presentation.presenter.CrucialInformationPresenter
import com.judge.core.util.tick.TickHandler
import com.judge.core.util.tick.TickHandlerImpl
import com.judge.qualimaster.util.ClockImpl
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CrucialInformationViewModel constructor(
    repository: Repository, competitionId: Long
) : ViewModel() {

    // private val competitionId = 1L
    private val tickHandler: TickHandler = TickHandlerImpl(ClockImpl())
    private val athleteBlock = AthleteBlockUseCase()

    private val interactor = CrucialInformationInteractor(
            AthleteBoulderBlockUseCase(repository, athleteBlock),
            AthleteTransitBlockUseCase(repository, athleteBlock),
            SetStartTimeUseCase(repository),
            SubscribeCurrentTimeUseCase(tickHandler),
            SubscribeCurrentRotationUseCaseImpl(
                SubscribeCompetitionUseCaseImpl(repository),
                SubscribeCurrentTimeUseCase(tickHandler),
                GetRotationUseCase(),
            ),
            SubscribeCompetitionUseCaseImpl(repository),
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