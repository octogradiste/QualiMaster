package com.judge.qualimaster.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    val name: String,
    val numOfAthletes: Int,
    val competitionId: Long,
    @PrimaryKey(autoGenerate = true) val categoryId: Long = 0L
)