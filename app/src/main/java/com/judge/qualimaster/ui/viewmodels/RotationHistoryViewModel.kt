package com.judge.qualimaster.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.judge.core.data.Repository
import com.judge.core.interactor.RotationHistoryInteractor
import com.judge.core.interactor.usecase.athlete.AthleteBlockUseCase
import com.judge.core.interactor.usecase.athlete.AthleteBoulderBlockUseCase
import com.judge.core.interactor.usecase.competition.RefreshCompetitionUseCase
import com.judge.core.interactor.usecase.competition.SubscribeCompetitionUseCaseImpl
import com.judge.core.presentation.presenter.RotationHistoryPresenter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class RotationHistoryViewModel(repository: Repository, competitionId: String) : ViewModel() {
    // private val competitionId = 1L

    private val interactor = RotationHistoryInteractor(
            AthleteBoulderBlockUseCase(repository, AthleteBlockUseCase()),
            SubscribeCompetitionUseCaseImpl(repository),
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