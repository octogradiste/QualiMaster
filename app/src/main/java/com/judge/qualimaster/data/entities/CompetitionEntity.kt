package com.judge.qualimaster.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "competitions")
data class CompetitionEntity(
    val name: String,
    val location: String,
    val startTime: Long,
    val minPerBoulder: Int,
    val numOfAthletesClimbing: Int,
    val numOfAthletesInBuffer: Int,
    @PrimaryKey val competitionId: String,
)
