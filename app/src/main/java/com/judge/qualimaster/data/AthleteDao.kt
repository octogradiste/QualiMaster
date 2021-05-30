package com.judge.qualimaster.data

import androidx.room.*
import com.judge.core.domain.model.Category
import com.judge.core.domain.model.Competition
import com.judge.qualimaster.data.entities.AthleteEntity
import com.judge.qualimaster.data.entities.CategoryEntity
import com.judge.qualimaster.data.entities.CompetitionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface AthleteDao {

    @Insert
    suspend fun insertAthletes(athletes: List<AthleteEntity>)

    @Insert
    suspend fun insertCategories(categories: List<CategoryEntity>)

    @Insert
    suspend fun insertCompetitions(competitions: List<CompetitionEntity>)

    @Update
    suspend fun updateCompetition(comp: CompetitionEntity)

    @Query("SELECT * FROM competitions WHERE competitionId == :competitionId")
    fun subscribeCompetition(competitionId: Long): Flow<CompetitionEntity?>

    @Query("SELECT * FROM categories WHERE competitionId == :competitionId")
    suspend fun getCategories(competitionId: Long): List<CategoryEntity?>

    @Query("SELECT * FROM athletes WHERE competitionId == :competitionId AND startOrder in (:order)")
    suspend fun getAthletesByStartOrder(competitionId: Long, order: List<Int>): List<AthleteEntity?>

}