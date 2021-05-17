package com.judge.qualimaster.data

import com.judge.core.data.BaseRepository
import com.judge.core.domain.model.Athlete
import com.judge.core.domain.model.Category
import com.judge.core.domain.model.Competition
import com.judge.core.domain.result.Response
import com.judge.core.domain.result.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BaseRepositoryImpl : BaseRepository {

    private val category = Category(0, "c1", 3)
    private val athletes = listOf(
            Athlete(1, 0, "Adam", "Ondra", 1234, category),
            Athlete(3, 1, "Alex", "Megos", 2345, category),
            Athlete(2, 2, "Magnus", "Mitboe", 3456, category)
    )

    private var comp = Competition(
            0,
            "test",
            "jvm",
            1620797607984L,
            5,
            5,
            1,
            listOf(category)
    )

    private val compState = MutableStateFlow(comp)

    override suspend fun sync(): Response = Response.Success("Successfully synced data.")

    override suspend fun refreshCompetition(competitionId: Int): Response = Response.Success("Successfully refreshed competition.")

    override suspend fun setStartTime(competitionId: Int, time: Long): Response {
        compState.value = compState.value.copy(startTime = time)
        return Response.Success("Successfully set start time")
    }

    override fun subscribeCompetition(competitionId: Int): StateFlow<Competition> = compState.asStateFlow()

    override suspend fun getAthletesByStartOrder(competitionId: Int, order: List<Int>, category: Category): Result<List<Athlete>> {
        return Result.Success(athletes
            .filter { athlete -> athlete.startOrder in order && athlete.category == category }
            .sortedBy { athlete -> athlete.category.name }
            .sortedBy { athlete -> athlete.startOrder})
    }
}