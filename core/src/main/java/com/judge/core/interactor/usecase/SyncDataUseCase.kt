package com.judge.core.interactor.usecase

import com.judge.core.data.Repository

class SyncDataUseCase(
        private val repository: Repository
) {
    suspend operator fun invoke() = repository.sync()
}