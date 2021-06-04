package com.judge.qualimaster.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "categories",
    primaryKeys = ["competitionId", "name"],
)
data class CategoryEntity(
    val name: String,
    val numOfAthletes: Int,
    val competitionId: String,
)