package com.judge.qualimaster.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.judge.qualimaster.fakes.FakeAthletes.honnold
import com.judge.qualimaster.fakes.FakeAthletes.megos
import com.judge.qualimaster.fakes.FakeAthletes.mitbo
import com.judge.qualimaster.fakes.FakeAthletes.ondra
import com.judge.qualimaster.fakes.FakeAthletes.sharma
import com.judge.qualimaster.fakes.FakeAthletes.woods
import com.judge.qualimaster.getOrAwaitValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@SmallTest
class BaseRepositoryTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var repository: Repository

    @Before
    fun setup() = runBlocking {
        hiltRule.inject()
        repository.insertAthletes(listOf(megos, mitbo, ondra))
    }

    @Test
    fun getAllAthletes() {
        val athletes = repository.getAllAthletes().asLiveData().getOrAwaitValue()
        assertThat(athletes).hasSize(3)
        assertThat(athletes).containsExactly(megos, mitbo, ondra)
    }

    @Test
    fun getAthlete() {
        val athlete = repository.getAthlete(1)
        assertThat(athlete).isEqualTo(megos)
    }

    @Test
    fun getAthleteByStartNumber() {
        val athlete = repository.getAthleteByStartNumber(4)
        assertThat(athlete).isEqualTo(megos)
    }

    @Test
    fun getAthletesByStartNumberOrdered() {
        val athletes = repository.getAthletesByStartNumber(listOf(4, 2, 1), true)
        assertThat(athletes).hasSize(3)
        assertThat(athletes).containsExactly(megos, mitbo, ondra).inOrder()
    }

    @Test
    fun getAthletesByStartNumber() {
        val athletes = repository.getAthletesByStartNumber(listOf(4, 2))
        assertThat(athletes).hasSize(2)
        assertThat(athletes).containsExactly(megos, mitbo)
    }

    @Test
    fun countAthletes() {
        assertThat(repository.countAthletes()).isEqualTo(3)
    }

    @Test
    fun insertAthlete() = runBlocking {
        repository.insertAthlete(sharma)
        val athletes = repository.getAllAthletes().asLiveData().getOrAwaitValue()
        assertThat(athletes).contains(sharma)
    }

    @Test
    fun insertAthleteOnConflictReplaceStrategy() = runBlocking {
        repository.insertAthlete(woods)
        repository.insertAthlete(honnold)
        val athletes = repository.getAllAthletes().asLiveData().getOrAwaitValue()
        assertThat(athletes).contains(honnold)
        assertThat(athletes).doesNotContain(woods)
    }

    @Test
    fun insertAthletes(): Unit = runBlocking {
        repository.insertAthletes(listOf(sharma, woods))
        val athletes = repository.getAllAthletes().asLiveData().getOrAwaitValue()
        assertThat(athletes).hasSize(5)
        assertThat(athletes).containsExactly(megos, mitbo, ondra, sharma, woods)
    }

    @Test
    fun deleteAthlete(): Unit = runBlocking {
        repository.deleteAthlete(megos)
        val athletes = repository.getAllAthletes().asLiveData().getOrAwaitValue()
        assertThat(athletes).hasSize(2)
        assertThat(athletes).containsExactly(mitbo, ondra)
    }
}