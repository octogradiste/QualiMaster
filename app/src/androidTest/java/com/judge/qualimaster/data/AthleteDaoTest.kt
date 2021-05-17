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
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@SmallTest
class AthleteDaoTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var db: AppDatabase

    lateinit var dao: AthleteDao

    @Before
    fun setup() = runBlocking {
        hiltRule.inject()
        dao = db.athleteDao()
        dao.insertAthletes(listOf(megos, mitbo, ondra))
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun getAllAthletes() {
        val athletes = dao.getAllAthletes().asLiveData().getOrAwaitValue()
        assertThat(athletes).hasSize(3)
        assertThat(athletes).containsExactly(megos, mitbo, ondra)
    }
    
    @Test
    fun getAthlete() {
        val athlete = dao.getAthlete(1)
        assertThat(athlete).isEqualTo(megos)
    }

    @Test
    fun getAthleteByStartNumber() {
        val athlete = dao.getAthleteByStartNumber(4)
        assertThat(athlete).isEqualTo(megos)
    }

    @Test
    fun getAthletesByStartNumberOrdered() {
        val athletes = dao.getAthletesByStartNumberOrdered(listOf(4, 2, 1))
        assertThat(athletes).hasSize(3)
        assertThat(athletes).containsExactly(megos, mitbo, ondra).inOrder()
    }

    @Test
    fun getAthletesByStartNumber() {
        val athletes = dao.getAthletesByStartNumber(listOf(4, 2))
        assertThat(athletes).hasSize(2)
        assertThat(athletes).containsExactly(megos, mitbo)
    }

    @Test
    fun countAthletes() {
        assertThat(dao.countAthletes()).isEqualTo(3)
    }

    @Test
    fun insertAthlete() = runBlocking {
        dao.insertAthlete(sharma)
        val athletes = dao.getAllAthletes().asLiveData().getOrAwaitValue()
        assertThat(athletes).contains(sharma)
    }

    @Test
    fun insertAthleteOnConflictReplaceStrategy() = runBlocking {
        dao.insertAthlete(woods)
        dao.insertAthlete(honnold)
        val athletes = dao.getAllAthletes().asLiveData().getOrAwaitValue()
        assertThat(athletes).contains(honnold)
        assertThat(athletes).doesNotContain(woods)
    }

    @Test
    fun insertAthletes(): Unit = runBlocking {
        dao.insertAthletes(listOf(sharma, woods))
        val athletes = dao.getAllAthletes().asLiveData().getOrAwaitValue()
        assertThat(athletes).hasSize(5)
        assertThat(athletes).containsExactly(megos, mitbo, ondra, sharma, woods)
    }

    @Test
    fun deleteAthlete(): Unit = runBlocking {
        dao.deleteAthlete(megos)
        val athletes = dao.getAllAthletes().asLiveData().getOrAwaitValue()
        assertThat(athletes).hasSize(2)
        assertThat(athletes).containsExactly(mitbo, ondra)
    }

}