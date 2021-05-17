package com.judge.core.presentation.presenter

import com.judge.core.domain.result.Response
import com.judge.core.domain.result.Result
import com.judge.core.interactor.CrucialInformationInteractor
import com.judge.core.presentation.AthleteListItem
import com.judge.core.presentation.state.AthleteListState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CrucialInformationPresenter (
        private val competitionId: Int,
        private val interactor: CrucialInformationInteractor,
        private val ioDispatcher: CoroutineDispatcher,
        externalScope: CoroutineScope,
) {

    private val _startTime = MutableStateFlow(0L)
    private val _crucialInformation = MutableStateFlow(emptyList<AthleteListItem>())
    private val _athleteListState = MutableStateFlow<AthleteListState>(AthleteListState.Loading)

    private val comp = interactor.subscribeCompetition(competitionId)

    val startTime = _startTime.asStateFlow()
    val crucialInformation = _crucialInformation.asStateFlow()
    val athleteListState = _athleteListState.asStateFlow()

    val currentTime = interactor.subscribeCurrentTime()
    val currentRotation = interactor.subscribeCurrentRotation(competitionId)

    init {
        externalScope.launch {
            // refresh()

            comp.collect { comp ->
                _startTime.value = comp.startTime
            }

            currentRotation.collect { rotation ->
                withContext(ioDispatcher) {
                    when(val result = interactor.createCrucialInformation(rotation, comp.value)) {
                        is Result.Success -> _crucialInformation.value = result.value
                        is Result.Error -> {
                            _crucialInformation.value = emptyList()
                            _athleteListState.value = AthleteListState.Error(result.msg)
                        }
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

    /**
     * Sets the start time of the competition to [time].
     * Note: This is done on the [ioDispatcher]
     */
    suspend fun setStartTime(time: Long): Response {
        return withContext(ioDispatcher) {
            return@withContext interactor.setStartTime(competitionId, time)
        }
    }
}