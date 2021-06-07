package com.judge.core.interactor.usecase.competition

import com.judge.core.data.Repository
import com.judge.core.domain.model.Athlete
import com.judge.core.domain.model.Competition

class UploadNewCompetitionUseCase(private val repository: Repository) {
    suspend operator fun invoke(competition: Competition, athletes: List<Athlete>) =
        repository.uploadNewCompetition(competition, athletes)
}