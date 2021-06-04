package com.judge.qualimaster.data
/*
import com.judge.core.data.BaseRepository
import com.judge.core.domain.model.Athlete
import com.judge.core.domain.model.Category
import com.judge.core.domain.model.Competition
import com.judge.core.domain.result.Response
import com.judge.core.domain.result.Result
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

class LocalBaseRepositoryImpl(
    private val athleteDao: AthleteDao,
) : BaseRepository {

//    private val competitionId = 1
//    private val category = Category(1, "c1", 4, 1)
//    private val athletes = listOf(
//            Athlete(1, 0, "Adam", "Ondra", 1234, category, 1),
//            Athlete(31, 1, "Alex", "Megos", 2345, category, 1),
//            Athlete(12, 2, "Magnus", "Mitboe", 3456, category, 1),
//            Athlete(15, 3, "Sascha", "Lehmann", 4567, category, 1),
//    )
//    private val competition = Competition(
//            1,
//            "test",
//            "jvm",
//            1621490788852L,
//            5,
//            5,
//            1,
//            listOf(category)
//    )

    override suspend fun getAllCompetitions(): Result<List<Competition>> {
        val competitionEntities = athleteDao.getAllCompetitions()
        val competitions = competitionEntities.map { competitionEntity ->
            if (competitionEntity == null) {
                return Result.Error("Null Competition Entity")
            } else {
                val categories = athleteDao.getCategories(competitionEntity.competitionId)
                categories.map { categoryEntity ->
                    categoryEntity?.toCategory() ?: return Result.Error("Null Category Entity")
                }
                competitionEntity.toCompetition(categories.map { categoryEntity ->
                    categoryEntity?.toCategory() ?: return Result.Error("Null Category Entity")
                })
            }
        }
        return Result.Success(competitions)
    }

    override suspend fun insertAthletes(athletes: List<Athlete>): Response {
        athleteDao.insertAthletes(athletes.map { it.toEntity() })
        return Response.Success("Successfully inserted athletes")
    }

    override suspend fun insertCategories(categories: List<Category>): Response {
        athleteDao.insertCategories(categories.map { it.toEntity() })
        return Response.Success("Successfully inserted categories")
    }

    override suspend fun insertCompetitions(competitions: List<Competition>): Response {
        athleteDao.insertCompetitions(competitions.map { it.toEntity() })
        return Response.Success("Successfully inserted competitions")
    }

    override suspend fun sync(): Response {
        return Response.Success("Successfully synced data.")
    }

    override suspend fun refreshCompetition(competitionId: String): Response {
        return Response.Success("Successfully refreshed competition.")
    }

    override suspend fun setStartTime(competition: Competition, time: Long): Response {
        athleteDao.updateCompetition(competition.copy(startTime = time).toEntity())
        return Response.Success("Successfully set start time")
    }

    override suspend fun subscribeCompetition(
        competitionId: String,
        externalScope: CoroutineScope
    ): StateFlow<Result<Competition>> {
        val subscription = athleteDao.subscribeCompetition(competitionId)
        return subscription
            .map { entity ->
                if (entity == null) {
                    Result.Error("No id $competitionId")
                } else {
                    val categories = athleteDao.getCategories(competitionId)
                        .filterNotNull()
                        .map { it.toCategory() }
                    if (categories.isEmpty()) {
                        Result.Error("No category for competition $competitionId")
                    }
                    Result.Success(entity.toCompetition(categories))
                }
            }
            .stateIn(externalScope.apply { coroutineContext + CoroutineName("Subscribe Competition Base Repository") })
    }

    override suspend fun getAthletesByStartOrder(competitionId: String, order: List<Int>): Result<List<Athlete>> {
        val athletes = athleteDao.getAthletesByStartOrder(competitionId, order)
        return Result.Success(athletes.filterNotNull().map { it.toAthlete()})
    }
}
*/