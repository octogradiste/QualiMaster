package com.judge.core.presentation.presenter

import com.judge.core.interactor.HomeInteractor

class HomePresenter(
    private val homeInteractor: HomeInteractor
) {

    suspend fun getAllCompetitions() = homeInteractor.getAllCompetitions()

}