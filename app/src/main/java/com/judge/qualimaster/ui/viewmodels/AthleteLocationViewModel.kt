package com.judge.qualimaster.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.judge.core.data.Repository
import com.judge.core.interactor.AthleteLocationInteractor
import com.judge.core.interactor.usecase.*
import com.judge.core.interactor.usecase.athlete.*
import com.judge.core.interactor.usecase.competition.RefreshCompetitionUseCase
import com.judge.core.interactor.usecase.competition.SubscribeCompetitionUseCaseImpl
import com.judge.core.interactor.usecase.rotation.GetRotationUseCase
import com.judge.core.interactor.usecase.rotation.SubscribeCurrentRotationUseCaseImpl
import com.judge.core.presentation.presenter.AthleteLocationPresenter
import com.judge.core.util.tick.TickHandlerImpl
import com.judge.qualimaster.util.ClockImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class AthleteLocationViewModel(repository: Repository, competitionId: String): ViewModel() {
    //private val competitionId = 1L
    private val athleteBlock = AthleteBlockUseCase()

    private val interactor = AthleteLocationInteractor(
            AthleteBoulderBlockUseCase(repository, athleteBlock),
            AthleteTransitBlockUseCase(repository, athleteBlock),
            AthleteMovingBlockUseCase(repository, athleteBlock),
            AthleteIsolationBlockUseCase(repository, athleteBlock),
            SubscribeCurrentRotationUseCaseImpl(
                SubscribeCompetitionUseCaseImpl(repository),
                SubscribeCurrentTimeUseCase(TickHandlerImpl(ClockImpl())),
                GetRotationUseCase(),
            ),
            SubscribeCompetitionUseCaseImpl(repository),
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