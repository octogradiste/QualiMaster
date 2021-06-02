package com.judge.qualimaster.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.judge.core.data.Repository
import com.judge.core.interactor.HomeInteractor
import com.judge.core.interactor.usecase.competition.GetAllCompetitionsUseCase
import com.judge.core.presentation.presenter.HomePresenter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

  private val homePresenter = HomePresenter(
    HomeInteractor(GetAllCompetitionsUseCase(repository))
  )

  suspend fun getAllCompetitions() = homePresenter.getAllCompetitions()

}