package com.judge.core.domain

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class RotationPeriodTest {

    @Nested
    @DisplayName("rotationFromCurrentTime method")
    inner class RotationFromCurrentTime {

        @ParameterizedTest
        @ValueSource(ints = [60_000, 240_000, 300_000])
        fun `independent of minutes per boulder smaller current time as start time, returns 0`(minutesPerBoulder: Int) {
            val rotation = RotationPeriod.rotationFromCurrentTime(100_000_000, 90_000_000, minutesPerBoulder)
            assertThat(rotation).isEqualTo(0)
        }

        @ParameterizedTest
        @ValueSource(ints = [60_000, 240_000, 300_000])
        fun `independent of minutes per boulder ame current time as start time, returns 1`(minutesPerBoulder: Int) {
            val rotation = RotationPeriod.rotationFromCurrentTime(100_000_000, 100_000_000, minutesPerBoulder)
            assertThat(rotation).isEqualTo(1)
        }

        @Test
        fun `start time 32e9, current time 4min later with 4 min per boulder, returns 2`() {
            val startTime = 32_000_000_000
            val rotation = RotationPeriod.rotationFromCurrentTime(startTime, startTime + 4*60*1000, 4)
            assertThat(rotation).isEqualTo(2)
        }

        @Test
        fun `start time 140e9, current time 4min later with 5 min per boulder, returns 2`() {
            val startTime = 140_000_000_000
            val rotation = RotationPeriod.rotationFromCurrentTime(startTime, startTime + 5*60*1000, 5)
            assertThat(rotation).isEqualTo(2)
        }

        @Test
        fun `start time 32e9, current time 9min later with 4 min per boulder, returns 3`() {
            val startTime = 32_000_000_000
            val rotation = RotationPeriod.rotationFromCurrentTime(startTime, startTime + 9*60*1000, 4)
            assertThat(rotation).isEqualTo(3)
        }

    }

    @Nested
    @DisplayName("maxRotation method")
    inner class MaxRotation {

        @Test
        fun `3 athletes climbing and 6 participants, returns 10`() {
            val max = RotationPeriod.maxRotation(3, 6)
            assertThat(max).isEqualTo(10)
        }

        @Test
        fun `5 athletes climbing and 1 participants, returns 9`() {
            val max = RotationPeriod.maxRotation(5, 1)
            assertThat(max).isEqualTo(9)
        }

        @Test
        fun `5 athletes climbing and 12 participants, returns 9`() {
            val max = RotationPeriod.maxRotation(5, 12)
            assertThat(max).isEqualTo(20)
        }

    }

}