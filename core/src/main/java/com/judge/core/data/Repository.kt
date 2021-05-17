package com.judge.core.data

import com.judge.core.domain.Location
import com.judge.core.domain.model.Athlete
import com.judge.core.domain.model.Category
import com.judge.core.domain.model.Competition
import com.judge.core.domain.result.Result

class Repository(
        private val baseRepository: BaseRepository,
        private val location: Location
) {

    suspend fun sync() = baseRepository.sync()

    suspend fun refreshCompetition(competitionId: Int) =
            baseRepository.refreshCompetition(competitionId)

    suspend fun setStartTime(competitionId: Int, time: Long) =
            baseRepository.setStartTime(competitionId, time)

    fun subscribeCompetition(competitionId: Int) =
            baseRepository.subscribeCompetition(competitionId)

    suspend fun getAthletesInIsolation(
            rotation: Int, comp: Competition, category: Category
    ) : Result<List<Athlete>>{
        val first = location.firstAthleteInIsolation(rotation, comp.numOfAthletesInBuffer)
        val order = location.athletesInIsolation(first, category.numOfAthletes)
        val valid = location.extractValidAthletes(order, category.numOfAthletes)
        return if (valid.isNotEmpty()) {
            baseRepository.getAthletesByStartOrder(comp.competitionId, valid, category)
        } else {
            Result.Success(emptyList())
        }

    }

    suspend fun getAthletesClimbing(
            rotation: Int, comp: Competition, category: Category
    ) : Result<List<Athlete>> {
        val order = location.athletesClimbing(rotation, comp.numOfAthletesClimbing)
        val valid = location.extractValidAthletes(order, category.numOfAthletes)
        return if (valid.isNotEmpty()) {
            baseRepository.getAthletesByStartOrder(comp.competitionId, valid, category)
        } else {
            Result.Success(emptyList())
        }
    }

    suspend fun getAthletesMoving(
            rotation: Int, comp: Competition, category: Category
    ) : Result<List<Athlete>> {
        val order = location.athleteMoving(rotation, comp.numOfAthletesInBuffer)
        return if (location.isValidAthlete(order, category.numOfAthletes)) {
            baseRepository.getAthletesByStartOrder(comp.competitionId, listOf(order), category)
        } else {
            Result.Success(emptyList())
        }
    }

    suspend fun getAthletesInTransitZone(
            rotation: Int, comp: Competition, category: Category
    ) : Result<List<Athlete>> {
        val order = location.athletesInTransitZone(
                rotation, category.numOfAthletes, comp.numOfAthletesInBuffer)
        val valid = location.extractValidAthletes(order, category.numOfAthletes)
        return if (valid.isNotEmpty()) {
            baseRepository.getAthletesByStartOrder(comp.competitionId, valid, category)
        } else {
            Result.Success(emptyList())
        }
    }

}