package com.judge.core.domain.model

data class Athlete(
        val competitionId: String,
        val number: Int,
        val startOrder: Int,
        val firstName: String,
        val lastName: String,
        val licence: Int,
        val categoryName: String,
)
