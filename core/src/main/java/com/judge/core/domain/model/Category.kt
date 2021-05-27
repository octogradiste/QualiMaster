package com.judge.core.domain.model

data class Category(
        val categoryId: Long,
        val name: String,
        val numOfAthletes: Int,
        val competitionId: Long,
)
