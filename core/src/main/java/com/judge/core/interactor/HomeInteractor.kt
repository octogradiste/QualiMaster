package com.judge.core.interactor

import com.judge.core.interactor.usecase.competition.GetAllCompetitionsUseCase
import com.judge.core.interactor.usecase.competition.UploadNewCompetitionUseCase

class HomeInteractor(
    val getAllCompetitions: GetAllCompetitionsUseCase,
    val uploadNewCompetition: UploadNewCompetitionUseCase)