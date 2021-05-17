package com.judge.core.domain.model

data class Athlete(
        val number: Int,
        val startOrder: Int,
        val firstName: String,
        val lastName: String,
        val licence: Int,
        val category: Category,
)
