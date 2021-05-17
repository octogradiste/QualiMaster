package com.judge.core.presentation.presenter

import com.judge.core.domain.result.Response
import com.judge.core.domain.result.Result
import com.judge.core.interactor.RotationHistoryInteractor
import com.judge.core.presentation.AthleteListItem
import com.judge.core.presentation.state.AthleteListState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RotationHistoryPresenter(
        private val competitionId: Int,
        private val interactor: RotationHistoryInteractor,
        private val ioDispatcher: CoroutineDispatcher,
        externalScope: CoroutineScope,
) {
    private val comp = interactor.subscribeCompetition(competitionId)

    private val _rotationHistory = MutableStateFlow(emptyList<AthleteListItem>())
    private val _athleteListState = MutableStateFlow<AthleteListState>(AthleteListState.Loading)

    val rotationHistory = _rotationHistory.asStateFlow()
    val athleteListState = _athleteListState.asStateFlow()

    init {
        externalScope.launch {
            // refresh()

            // TODO when refreshing show rebuild list

            withContext(ioDispatcher) {
                when (val result = interactor.createRotationHistory(comp.value)) {
                    is Result.Success -> _rotationHistory.value = result.value
                    is Result.Error -> {
                        _rotationHistory.value = emptyList()
                        _athleteListState.value = AthleteListState.Error(result.msg)
                    }
                }
            }

        }
    }

    /**
     * Refreshes the competition and athlete data.
     * Note: This is done on the [ioDispatcher]
     */
    suspend fun refresh(): Response {
        _athleteListState.value = AthleteListState.Loading
        val response = withContext(ioDispatcher) {
            return@withContext interactor.refreshCompetition(competitionId)
        }
        _athleteListState.value = AthleteListState.Active
        return response
    }
}