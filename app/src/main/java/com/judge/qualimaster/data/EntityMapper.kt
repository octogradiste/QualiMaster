package com.judge.qualimaster.data

import com.judge.core.domain.model.Athlete
import com.judge.core.domain.model.Category
import com.judge.core.domain.model.Competition
import com.judge.qualimaster.data.entities.AthleteEntity
import com.judge.qualimaster.data.entities.CategoryEntity
import com.judge.qualimaster.data.entities.CompetitionEntity

fun AthleteEntity.toAthlete(): Athlete {
    return Athlete(number, startOrder, firstName, lastName, licence, category, competitionId)
}

fun Athlete.toEntity(): AthleteEntity {
    return AthleteEntity(competitionId, number, startOrder, firstName, lastName, licence, category)
}

fun CategoryEntity.toCategory(): Category {
    return Category(categoryId, name, numOfAthletes, competitionId)
}

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(name, numOfAthletes, competitionId, categoryId)
}

fun CompetitionEntity.toCompetition(categories: List<Category>): Competition {
    return Competition(
        competitionId, name, location, startTime, minPerBoulder, numOfAthletesClimbing,
        numOfAthletesInBuffer, categories)
}

fun Competition.toEntity(): CompetitionEntity {
    return CompetitionEntity(
        name, location, startTime, minPerBoulder, numOfAthletesClimbing,
        numOfAthletesInBuffer, competitionId)
}