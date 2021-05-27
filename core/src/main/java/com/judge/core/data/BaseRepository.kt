package com.judge.core.data

import com.judge.core.domain.model.Athlete
import com.judge.core.domain.model.Competition
import com.judge.core.domain.model.Category
import com.judge.core.domain.result.Response
import com.judge.core.domain.result.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow


interface BaseRepository {

    /**
     * Updates all the local data with the network data
     */
    suspend fun sync(): Response

    suspend fun insertAthletes(athletes: List<Athlete>): Response

    suspend fun insertCategories(categories: List<Category>): Response

    suspend fun insertCompetitions(competitions: List<Competition>): Response

    suspend fun refreshCompetition(competitionId: Long): Response

    suspend fun setStartTime(competition: Competition, time: Long): Response

    suspend fun subscribeCompetition(competitionId: Long, externalScope: CoroutineScope): StateFlow<Competition>

    suspend fun getAthletesByStartOrder(
            competitionId: Long,
            order: List<Int>,
    ): Result<List<Athlete>>

}