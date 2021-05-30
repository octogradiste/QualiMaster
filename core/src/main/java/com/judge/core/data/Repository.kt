package com.judge.core.data

import com.judge.core.domain.Location
import com.judge.core.domain.model.Athlete
import com.judge.core.domain.model.Competition
import com.judge.core.domain.result.Result
import kotlinx.coroutines.CoroutineScope

class Repository(
        private val baseRepository: BaseRepository,
        private val location: Location
) {

    suspend fun sync() = baseRepository.sync()

    suspend fun refreshCompetition(competitionId: Long) =
            baseRepository.refreshCompetition(competitionId)

    suspend fun setStartTime(competition: Competition, time: Long) =
            baseRepository.setStartTime(competition, time)

    suspend fun subscribeCompetition(competitionId: Long, externalScope: CoroutineScope) =
            baseRepository.subscribeCompetition(competitionId, externalScope)

    suspend fun getAthletesInIsolation(
            rotation: Int, comp: Competition
    ) : Result<List<Athlete>>{
        val first = location.firstAthleteInIsolation(rotation, comp.numOfAthletesInBuffer)
        val order = location.athletesInIsolation(first, maxAthleteInCategories(comp))
        val valid = location.extractValidAthletes(order, maxAthleteInCategories(comp))
        return if (valid.isNotEmpty()) {
            baseRepository.getAthletesByStartOrder(comp.competitionId, valid)
        } else {
            Result.Success(emptyList())
        }
    }

    suspend fun getAthletesClimbing(
            rotation: Int, comp: Competition
    ) : Result<List<Athlete>> {
        val order = location.athletesClimbing(rotation, comp.numOfAthletesClimbing)
        val valid = location.extractValidAthletes(order, maxAthleteInCategories(comp))
        return if (valid.isNotEmpty()) {
            baseRepository.getAthletesByStartOrder(comp.competitionId, valid)
        } else {
            Result.Success(emptyList())
        }
    }

    suspend fun getAthletesMoving(
            rotation: Int, comp: Competition
    ) : Result<List<Athlete>> {
        val order = location.athleteMoving(rotation, comp.numOfAthletesInBuffer)
        return if (location.isValidAthlete(order, maxAthleteInCategories(comp))) {
            baseRepository.getAthletesByStartOrder(comp.competitionId, listOf(order))
        } else {
            Result.Success(emptyList())
        }
    }

    suspend fun getAthletesInTransitZone(
            rotation: Int, comp: Competition
    ) : Result<List<Athlete>> {
        val order = location.athletesInTransitZone(
                rotation, comp.numOfAthletesClimbing, comp.numOfAthletesInBuffer)
        val valid = location.extractValidAthletes(order, maxAthleteInCategories(comp))
        return if (valid.isNotEmpty()) {
            baseRepository.getAthletesByStartOrder(comp.competitionId, valid)
        } else {
            Result.Success(emptyList())
        }
    }

    private fun maxAthleteInCategories(comp: Competition): Int {
        return if (comp.categories.isEmpty()) 0 else comp.categories.maxOf { it.numOfAthletes }
    }

}