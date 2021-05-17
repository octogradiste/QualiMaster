package com.judge.qualimaster.data

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import javax.inject.Inject

class Repository @Inject constructor(
    private val athleteDao: AthleteDao
) {

    fun getAllAthletes() = athleteDao.getAllAthletes()

    fun getAthlete(id: Int) = athleteDao.getAthlete(id)

    fun getAthleteByStartNumber(startNumber: Int) = athleteDao.getAthleteByStartNumber(startNumber)

    fun getAthletesByStartNumber(startNumbers: List<Int>, ordered: Boolean = false): List<Athlete> {
        if (ordered) {
            return athleteDao.getAthletesByStartNumberOrdered(startNumbers)
        }
        return athleteDao.getAthletesByStartNumber(startNumbers)
    }

    fun countAthletes() = athleteDao.countAthletes()

    suspend fun insertAthlete(athlete: Athlete) = athleteDao.insertAthlete(athlete)

    suspend fun insertAthletes(athletes: List<Athlete>) = athleteDao.insertAthletes(athletes)

    suspend fun deleteAthlete(athlete: Athlete) = athleteDao.deleteAthlete(athlete)

}