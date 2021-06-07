package com.judge.qualimaster.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.judge.core.data.Repository
import com.judge.core.domain.model.Athlete
import com.judge.core.domain.model.Competition
import com.judge.core.interactor.HomeInteractor
import com.judge.core.interactor.usecase.competition.GetAllCompetitionsUseCase
import com.judge.core.interactor.usecase.competition.UploadNewCompetitionUseCase
import com.judge.core.presentation.presenter.HomePresenter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(repository: Repository) : ViewModel() {

  private val homePresenter = HomePresenter(
    HomeInteractor(
      GetAllCompetitionsUseCase(repository),
      UploadNewCompetitionUseCase(repository)
    )
  )

  suspend fun getAllCompetitions() = homePresenter.getAllCompetitions()

  suspend fun uploadNewCompetition(competition: Competition, athletes: List<Athlete>) =
    homePresenter.uploadNewCompetition(competition, athletes)

}