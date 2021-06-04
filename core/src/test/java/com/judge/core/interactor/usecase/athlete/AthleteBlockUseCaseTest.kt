package com.judge.core.interactor.usecase.athlete

import com.google.common.truth.Truth.assertThat
import com.judge.core.domain.model.Athlete
import com.judge.core.domain.model.Category
import com.judge.core.domain.model.Competition
import com.judge.core.presentation.AthleteListItem
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AthleteBlockUseCaseTest {

    private val competitionId = 0L
    private val category1 = Category(0, "Men", 4, competitionId)
    private val category2 = Category(1, "Women", 5, competitionId)
    private val athletes = mutableListOf(
        Athlete(competitionId, 1, 0, "Adam", "Ondra", 1234, category1),
        Athlete(competitionId, 11, 1, "Alex", "Megos", 2345, category1),
        Athlete(competitionId, 12, 2, "Magnus", "Mitboe", 3456, category1),
        Athlete(competitionId, 15, 3, "Sascha", "Lehmann", 4567, category1),
        Athlete(competitionId, 23, 0, "Oriane", "Bertone", 1111, category2),
        Athlete(competitionId, 24, 1, "Janja", "Garnbert", 2222, category2),
        Athlete(competitionId, 28, 2, "Brooke", "Rabotu", 3333, category2),
        Athlete(competitionId, 29, 3, "Jessica", "Pilz", 4444, category2),
        Athlete(competitionId, 21, 4, "Petra", "Klinger", 5555, category2)
    )
    private val competition = Competition(
        competitionId, "Competition", "Location",
        0, 5, 5,
        1, listOf(category1, category2)
    )

    lateinit var athleteBlock: AthleteBlockUseCase

    @BeforeEach
    fun setup() {
        athleteBlock = AthleteBlockUseCase()
    }

    @Test
    fun `invoke method athlete category not in competition categories ignored`() {
        val notOriginalCategory = Category(3, "Not the original", 1, competitionId)
        val outsider = Athlete(competitionId, 7, 7, "Some", "One", 7, notOriginalCategory)
        val athleteList = athleteBlock("Empty", listOf(outsider), competition)
        assertThat(athleteList).isEmpty()
    }

    @Test
    fun `invoke method no athlete returns empty list`() {
        val athleteList = athleteBlock("No Athletes", emptyList(), competition)
        assertThat(athleteList).isEmpty()
    }

    @Test
    fun `invoke method only one category`() {
        val athletesOnlyOneCategory = athletes.filter { it.categoryName.categoryId == 0L }
        val athleteList = athleteBlock("Single", athletesOnlyOneCategory, competition)
        // Header
        assertThat(athleteList[0] is AthleteListItem.HeaderItem).isTrue()
        assertThat((athleteList[0] as AthleteListItem.HeaderItem).title).isEqualTo("Single")
        // Athletes of categoryId 0
        assertThat(athleteList[1] is AthleteListItem.CategoryItem).isTrue()
        assertThat((athleteList[1] as AthleteListItem.CategoryItem).category.name).isEqualTo("Men")
        for (i in 2..5) {
            assertThat((athleteList[i] as AthleteListItem.AthleteItem).athlete)
                .isEqualTo(athletesOnlyOneCategory.sortedByDescending { it.startOrder }[i - 2])
        }
    }

    @Test
    fun `invoke method multiple categories`() {
        val athleteList = athleteBlock("Multi", athletes, competition)
        val orderAthletes = athletes.sortedWith(compareBy<Athlete> { it.categoryName.categoryId }.thenByDescending { it.startOrder })
        // Header
        assertThat(athleteList[0] is AthleteListItem.HeaderItem).isTrue()
        assertThat((athleteList[0] as AthleteListItem.HeaderItem).title).isEqualTo("Multi")
        // Athletes of categoryId 0
        assertThat(athleteList[1] is AthleteListItem.CategoryItem).isTrue()
        assertThat((athleteList[1] as AthleteListItem.CategoryItem).category.name).isEqualTo("Men")
        for (i in 2..5) {
            assertThat((athleteList[i] as AthleteListItem.AthleteItem).athlete).isEqualTo(orderAthletes[i - 2])
        }
        // Athletes of categoryId 1
        assertThat(athleteList[6] is AthleteListItem.CategoryItem).isTrue()
        assertThat((athleteList[6] as AthleteListItem.CategoryItem).category.name).isEqualTo("Women")
        for (i in 7..11) {
            assertThat((athleteList[i] as AthleteListItem.AthleteItem).athlete).isEqualTo(orderAthletes[i - 3])
        }
    }

}