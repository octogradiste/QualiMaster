package com.judge.qualimaster.data

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AthleteDao {

    @Query("SELECT * FROM athletes")
    fun getAllAthletes(): Flow<List<Athlete>>

    @Query("SELECT * FROM athletes WHERE athleteNumber = :athleteNumber")
    fun getAthlete(athleteNumber: Int): Athlete?

    @Query("SELECT * FROM athletes WHERE startNumber = :startNumber")
    fun getAthleteByStartNumber(startNumber: Int) : Athlete?

    @Query("SELECT * FROM athletes WHERE startNumber IN (:startNumbers)")
    fun getAthletesByStartNumber(startNumbers: List<Int>): List<Athlete>

    @Query("SELECT * FROM athletes WHERE startNumber IN (:startNumbers) ORDER BY startNumber DESC")
    fun getAthletesByStartNumberOrdered(startNumbers: List<Int>): List<Athlete>

    @Query("SELECT COUNT(*) FROM athletes")
    fun countAthletes() : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAthlete(athlete: Athlete)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAthletes(athletes: List<Athlete>)

    @Delete
    suspend fun deleteAthlete(athlete: Athlete)

}