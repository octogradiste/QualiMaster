package com.judge.core.presentation.presenter

import com.judge.core.domain.model.Athlete
import com.judge.core.domain.model.Competition
import com.judge.core.interactor.HomeInteractor

class HomePresenter(
    private val homeInteractor: HomeInteractor
) {

    suspend fun getAllCompetitions() = homeInteractor.getAllCompetitions()

    suspend fun uploadNewCompetition(competition: Competition, athletes: List<Athlete>) =
        homeInteractor.uploadNewCompetition(competition, athletes)

}