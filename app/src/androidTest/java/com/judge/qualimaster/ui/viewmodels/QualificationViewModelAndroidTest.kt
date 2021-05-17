package com.judge.qualimaster.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.judge.qualimaster.R
import com.judge.qualimaster.adapter.BoulderQualificationListItem
import com.judge.qualimaster.data.Repository
import com.judge.qualimaster.fakes.FakeAthletes.fakeAthletesForCompetition
import com.judge.qualimaster.fakes.FakeIntervalTimer
import com.judge.qualimaster.getOrAwaitValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import javax.inject.Inject

@HiltAndroidTest
@SmallTest
class QualificationViewModelAndroidTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var repository: Repository
    lateinit var intervalTimer: FakeIntervalTimer
    lateinit var viewModel: QualificationViewModel


    @Before
    fun setup() = runBlocking {
        hiltRule.inject()
        repository.insertAthletes(fakeAthletesForCompetition)
        intervalTimer = FakeIntervalTimer()
        viewModel = QualificationViewModel(repository, intervalTimer)
    }

    private fun timeToMillis(hour: Int, minute: Int, seconds: Int): Long {
        val calendar = Calendar.getInstance().apply{
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, seconds)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    @Test
    fun testComputeCrucialInformationBeforeStartTime_returnsEmptyList() {
        viewModel.setStartTime(timeToMillis(12, 0, 0))
        intervalTimer.callIntervalTimerListener(timeToMillis(11,0,0))

        assertThat(viewModel.crucialInformation.getOrAwaitValue()).isEmpty()
    }

    @Test
    fun testComputeCrucialInformationAfterLastRotation_returnsEmptyList() {
        viewModel.setStartTime(timeToMillis(10, 0, 0))
        intervalTimer.callIntervalTimerListener(timeToMillis(11,30,0))

        assertThat(viewModel.crucialInformation.getOrAwaitValue()).isEmpty()
    }

    @Test
    fun testComputeCrucialInformationForRotation10() {
        viewModel.setStartTime(timeToMillis(10, 0, 0))
        intervalTimer.callIntervalTimerListener(timeToMillis(10,48,0))

        assertThat(viewModel.currentRotation.value).isEqualTo(10)
        assertThat(viewModel.crucialInformation.getOrAwaitValue()
                .filter { it.getType() == R.layout.viewholder_boulder }
                .map { qualificationListItem ->
                    val boulderQualificationListItem = qualificationListItem as BoulderQualificationListItem
                    boulderQualificationListItem.boulder.athlete.startNumber
                }
        ).containsExactlyElementsIn(listOf(10, 8, 6, 4, 2, 9, 7, 5, 3))
    }

}