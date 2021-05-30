package com.judge.core.data

import com.google.common.truth.Truth.assertThat
import com.judge.core.domain.Location
import com.judge.core.domain.model.Athlete
import com.judge.core.domain.model.Category
import com.judge.core.domain.model.Competition
import com.judge.core.domain.result.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class RepositoryTest {

    private val competitionId = 0L
    private val category = Category(0, "Category", 20, competitionId)
    private val athlete = Athlete(13, 0, "Test", "Dude", 1234, category, competitionId)
//    private val athletes = mutableListOf(
//        Athlete(1, 0, "Adam", "Ondra", 1234, category, competitionId),
//        Athlete(31, 1, "Alex", "Megos", 2345, category, competitionId),
//        Athlete(12, 2, "Magnus", "Mitboe", 3456, category, competitionId),
//        Athlete(15, 3, "Sascha", "Lehmann", 4567, category, competitionId),
//    )
    private val competition = Competition(
        competitionId, "Competition", "Location",
        0, 5, 5,
        1, listOf(category)
    )

    private lateinit var location: Location
    private lateinit var bastRepositoryMock: BaseRepository
    private lateinit var repository: Repository

    @BeforeEach
    fun setup() {
        location = Location
        bastRepositoryMock = mock(BaseRepository::class.java)
        repository = Repository(bastRepositoryMock, location)
    }

    @Test
    fun `getAthletesInIsolation makes correct success call to base repo`() = runBlockingTest {
        `when`(bastRepositoryMock.getAthletesByStartOrder(competitionId, (7..19).toList()))
            .thenReturn(Result.Success(listOf(athlete)))
        val res = repository.getAthletesInIsolation(5, competition)
        assertThat(res is Result.Success).isTrue()
        assertThat((res as Result.Success).value).containsExactly(athlete)
    }

    @Test
    fun `getAthletesInIsolation returns same error as base repo`() = runBlockingTest {
        val fakeError = Result.Error("Fake error for no good reason.")
        `when`(bastRepositoryMock.getAthletesByStartOrder(competitionId, (7..19).toList()))
            .thenReturn(fakeError)
        val res = repository.getAthletesInIsolation(5, competition)
        assertThat(res is Result.Error).isTrue()
        assertThat((res as Result.Error).msg).isEqualTo(fakeError.msg)
    }

    @Test
    fun `getAthletesClimbing makes correct success call to base repo`() = runBlockingTest {
        `when`(bastRepositoryMock.getAthletesByStartOrder(competitionId, listOf(1, 3, 5, 7, 9)))
            .thenReturn(Result.Success(listOf(athlete)))
        val res = repository.getAthletesClimbing(10, competition)
        assertThat(res is Result.Success).isTrue()
        assertThat((res as Result.Success).value).containsExactly(athlete)
    }

    @Test
    fun `getAthletesClimbing returns same error as base repo`() = runBlockingTest {
        val fakeError = Result.Error("Fake error for no good reason.")
        `when`(bastRepositoryMock.getAthletesByStartOrder(competitionId, listOf(1, 3, 5, 7, 9)))
            .thenReturn(fakeError)
        val res = repository.getAthletesClimbing(10, competition)
        assertThat(res is Result.Error).isTrue()
        assertThat((res as Result.Error).msg).isEqualTo(fakeError.msg)
    }

    @Test
    fun `getAthletesMoving makes correct success call to base repo`() = runBlockingTest {
        `when`(bastRepositoryMock.getAthletesByStartOrder(competitionId, listOf(16)))
            .thenReturn(Result.Success(listOf(athlete)))
        val res = repository.getAthletesMoving(15, competition)
        assertThat(res is Result.Success).isTrue()
        assertThat((res as Result.Success).value).containsExactly(athlete)
    }

    @Test
    fun `getAthletesMoving returns same error as base repo`() = runBlockingTest {
        val fakeError = Result.Error("Fake error for no good reason.")
        `when`(bastRepositoryMock.getAthletesByStartOrder(competitionId, listOf(16)))
            .thenReturn(fakeError)
        val res = repository.getAthletesMoving(15, competition)
        assertThat(res is Result.Error).isTrue()
        assertThat((res as Result.Error).msg).isEqualTo(fakeError.msg)
    }

    @Test
    fun `getAthletesInTransitZone makes correct success call to base repo`() = runBlockingTest {
        `when`(bastRepositoryMock.getAthletesByStartOrder(competitionId, listOf(0, 2, 4)))
            .thenReturn(Result.Success(listOf(athlete)))
        val res = repository.getAthletesInTransitZone(4, competition)
        assertThat(res is Result.Success).isTrue()
        assertThat((res as Result.Success).value).containsExactly(athlete)
    }

    @Test
    fun `getAthletesInTransitZone returns same error as base repo`() = runBlockingTest {
        val fakeError = Result.Error("Fake error for no good reason.")
        `when`(bastRepositoryMock.getAthletesByStartOrder(competitionId, listOf(0, 2, 4)))
            .thenReturn(fakeError)
        val res = repository.getAthletesInTransitZone(4, competition)
        assertThat(res is Result.Error).isTrue()
        assertThat((res as Result.Error).msg).isEqualTo(fakeError.msg)
    }
}