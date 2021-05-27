package com.judge.qualimaster.data.entities

import androidx.room.*
import com.judge.core.domain.model.Category

@Entity(
    tableName = "athletes",
    primaryKeys = ["competitionId", "number"],
)
data class AthleteEntity(
    val competitionId: Long,
    val number: Int,
    val startOrder: Int,
    val firstName: String,
    val lastName: String,
    val licence: Int,
    @Embedded(prefix = "category_") val category: Category
)