package com.judge.core.data

import com.judge.core.domain.model.Athlete
import com.judge.core.domain.model.Category
import com.judge.core.domain.model.Competition
import com.judge.core.domain.result.Response
import com.judge.core.domain.result.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BaseRepositoryFake() : BaseRepository {

    var athletes = mutableListOf<Athlete>()
    var categories = mutableListOf<Category>()
    val competitions = MutableStateFlow(mutableListOf<Competition>())

    fun populateWithFakeData() {

    }

    override suspend fun sync(): Response = Response.Success("Fake sync")

    override suspend fun insertAthletes(athletes: List<Athlete>): Response {
        this.athletes.addAll(athletes)
        return Response.Success("Fake athlete insert")
    }

    override suspend fun insertCategories(categories: List<Category>): Response {
        this.categories.addAll(categories)
        return Response.Success("Fake category insert")
    }

    override suspend fun insertCompetitions(competitions: List<Competition>): Response {
        this.competitions.value.addAll(competitions)
        return Response.Success("Fake competition insert")
    }

    override suspend fun refreshCompetition(competitionId: Long): Response =
        Response.Success("Fake competition refresh")

    override suspend fun setStartTime(competition: Competition, time: Long): Response {
        competitions.value.replaceAll {
            if (it.competitionId == competition.competitionId) competition.copy(startTime = time) else competition}
        return Response.Success("Fake start time update")
    }

    override suspend fun subscribeCompetition(
        competitionId: Long,
        externalScope: CoroutineScope
    ): StateFlow<Result<Competition>> {
        val competition = MutableStateFlow(competitions.value.find { it.competitionId == competitionId })
        externalScope.launch {
            competitions.collect { competitions ->
                competition.value = competitions.find { it.competitionId == competitionId }
            }
        }
        return competition
            .map {
                if (it == null) {
                    Result.Error("Fake competition is not available")
                } else {
                    Result.Success(it)
                }
            }
            .stateIn(externalScope)
    }

    override suspend fun getAthletesByStartOrder(
        competitionId: Long,
        order: List<Int>
    ): Result<List<Athlete>> {
        return Result.Success(athletes.filter { it.competitionId == competitionId && it.startOrder in order })
    }
}