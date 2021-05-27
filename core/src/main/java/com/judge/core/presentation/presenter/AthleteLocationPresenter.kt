package com.judge.core.presentation.presenter

import com.judge.core.domain.model.Competition
import com.judge.core.domain.result.Response
import com.judge.core.domain.result.Result
import com.judge.core.interactor.AthleteLocationInteractor
import com.judge.core.presentation.AthleteListItem
import com.judge.core.presentation.state.AthleteListState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.swing.plaf.nimbus.State

class AthleteLocationPresenter(
        private val competitionId: Long,
        private val interactor: AthleteLocationInteractor,
        private val ioDispatcher: CoroutineDispatcher,
        externalScope: CoroutineScope,
) {
    private val _athleteLocation = MutableStateFlow(emptyList<AthleteListItem>())
    private val _athleteListState = MutableStateFlow<AthleteListState>(AthleteListState.Loading)

    private lateinit var comp: StateFlow<Competition>
    private lateinit var currentRotation: StateFlow<Int>

    val athleteLocation = _athleteLocation.asStateFlow()
    val athleteListState = _athleteListState.asStateFlow()

    init {
        externalScope.launch {
            // refresh()
            comp = interactor.subscribeCompetition(competitionId)
            currentRotation = interactor.subscribeCurrentRotation(competitionId)
            currentRotation.collect { rotation ->
                withContext(ioDispatcher) {
                    when(val result = interactor.createAthleteLocationBlock(rotation, comp.value)) {
                        is Result.Success -> _athleteLocation.value = result.value
                        is Result.Error -> {
                            _athleteLocation.value = emptyList()
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
}