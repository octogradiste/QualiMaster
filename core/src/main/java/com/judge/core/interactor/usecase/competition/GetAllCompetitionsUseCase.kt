package com.judge.core.interactor.usecase.competition

import com.judge.core.data.Repository

class GetAllCompetitionsUseCase(
    private val repository: Repository
) {

    suspend operator fun invoke() = repository.getAllCompetitions()

}