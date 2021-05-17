package com.judge.core.data

import com.judge.core.domain.model.Athlete
import com.judge.core.domain.model.Competition
import com.judge.core.domain.model.Category
import com.judge.core.domain.result.Response
import com.judge.core.domain.result.Result
import kotlinx.coroutines.flow.StateFlow


interface BaseRepository {

    /**
     * Updates all the local data with the network data
     */
    suspend fun sync(): Response

    suspend fun refreshCompetition(competitionId: Int): Response

    suspend fun setStartTime(competitionId: Int, time: Long): Response

    fun subscribeCompetition(competitionId: Int): StateFlow<Competition>

    suspend fun getAthletesByStartOrder(
            competitionId: Int,
            order: List<Int>,
            category: Category
    ): Result<List<Athlete>>

}