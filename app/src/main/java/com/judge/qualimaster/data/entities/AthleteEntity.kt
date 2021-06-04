package com.judge.qualimaster.data.entities

import androidx.room.*

@Entity(
    tableName = "athletes",
    primaryKeys = ["competitionId", "number"],
)
data class AthleteEntity(
    val competitionId: String,
    val number: Int,
    val startOrder: Int,
    val firstName: String,
    val lastName: String,
    val licence: Int,
    val categoryName: String,
)