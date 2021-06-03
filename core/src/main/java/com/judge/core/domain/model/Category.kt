package com.judge.core.domain.model

data class Category(
        val categoryId: Int,
        val name: String,
        val numOfAthletes: Int,
        val competitionId: String,
)
