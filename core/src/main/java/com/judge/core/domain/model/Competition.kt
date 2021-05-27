package com.judge.core.domain.model

data class Competition(
        val competitionId: Long,
        val name: String,
        val location: String,
        val startTime: Long,
        val minPerBoulder: Int,
        val numOfAthletesClimbing: Int,
        val numOfAthletesInBuffer: Int,
        val categories: List<Category>,
)
