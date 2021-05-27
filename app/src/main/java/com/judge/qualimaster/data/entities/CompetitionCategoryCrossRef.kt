package com.judge.qualimaster.data.entities

import androidx.room.Entity

@Entity(primaryKeys = ["competitionId", "categoryId"])
data class CompetitionCategoryCrossRef(
    val competitionId: Long,
    val categoryId: Long,
)