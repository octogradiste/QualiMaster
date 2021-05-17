package com.judge.core.domain.model

data class Competition(
        val competitionId: Int,
        val name: String,
        val location: String,
        var startTime: Long,
        val minPerBoulder: Int,
        val numOfAthletesClimbing: Int,
        val numOfAthletesInBuffer: Int,
        val categories: List<Category>
)
