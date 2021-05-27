package com.judge.qualimaster.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.judge.core.domain.model.Athlete
import com.judge.core.domain.model.Competition

data class CompetitionWithAthletes(
    @Embedded val competition: Competition,
    @Relation(
        parentColumn = "competitionId",
        entityColumn = "competitionId"
    )
    val athletes: List<Athlete>
)
