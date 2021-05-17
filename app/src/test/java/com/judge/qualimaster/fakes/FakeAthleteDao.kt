package com.judge.qualimaster.fakes

import com.judge.qualimaster.data.Athlete
import com.judge.qualimaster.data.AthleteDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeAthleteDao(private val fakeAthletes: MutableList<Athlete>) : AthleteDao {


    override fun getAllAthletes(): Flow<List<Athlete>> {
        return flowOf(fakeAthletes)
    }

    override fun getAthlete(athleteNumber: Int): Athlete? {
        return fakeAthletes.find { athlete -> athlete.athleteNumber == athleteNumber } ?: null
    }

    override fun getAthleteByStartNumber(startNumber: Int): Athlete? {
        return fakeAthletes.find { athlete -> athlete.startNumber == startNumber } ?: null
    }

    override fun getAthletesByStartNumber(startNumbers: List<Int>): List<Athlete> {
        return fakeAthletes.filter { athlete -> athlete.startNumber in startNumbers }
    }

    override fun getAthletesByStartNumberOrdered(startNumbers: List<Int>): List<Athlete> {
        return getAthletesByStartNumber(startNumbers).sortedByDescending { athlete -> athlete.startNumber }
    }

    override fun countAthletes(): Int {
        return fakeAthletes.size
    }

    override suspend fun insertAthlete(athlete: Athlete) {
        fakeAthletes.add(athlete)
    }

    override suspend fun insertAthletes(athletes: List<Athlete>) {
        fakeAthletes.addAll(athletes)
    }

    override suspend fun deleteAthlete(athlete: Athlete) {
        fakeAthletes.remove(athlete)
    }
}