package com.judge.core.domain

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class LocationTest {

    @Nested
    @DisplayName("athletesClimbing method")
    inner class AthletesClimbingMethod {

        @Test
        fun `5 athletes climbing at rotation 1, returns -8, -6, -4, -2, 0`() {
            val athletes = Location.athletesClimbing(1, 5)
            assertThat(athletes).containsExactlyElementsIn(listOf(-8, -6, -4, -2, 0)).inOrder()
        }

        @Test
        fun `2 athletes climbing at rotation 1, returns -2, 0`() {
            val athletes = Location.athletesClimbing(1, 2)
            assertThat(athletes).containsExactlyElementsIn(listOf(-2, 0)).inOrder()
        }

        @Test
        fun `5 athletes climbing at rotation 11, returns 2, 4, 6, 8, 10`() {
            val athletes = Location.athletesClimbing(11, 5)
            assertThat(athletes).containsExactlyElementsIn(listOf(2, 4, 6, 8, 10)).inOrder()
        }

        @Test
        fun `2 athletes climbing at rotation 11, returns 8, 10`() {
            val athletes = Location.athletesClimbing(11, 2)
            assertThat(athletes).containsExactlyElementsIn(listOf(8, 10)).inOrder()
        }

        @ParameterizedTest
        @ValueSource(ints = [-11, -2, 1, 3, 5, 19, 23])
        fun `0 athletes climbing, returns an empty list`(rotation: Int) {
            val athletes = Location.athletesClimbing(rotation, 0)
            assertThat(athletes).isEmpty()
        }
    }

    @Nested
    @DisplayName("athletesResting method")
    inner class AthletesRestingMethod {

        @Test
        fun `4 athletes climbing at rotation 2, returns -4, -2, 0`() {
            val athletes = Location.athletesResting(2, 4)
            assertThat(athletes).containsExactlyElementsIn(listOf(-4, -2, 0)).inOrder()
        }

        @Test
        fun `2 athletes climbing at rotation 2, returns 0`() {
            val athletes = Location.athletesResting(2, 2)
            assertThat(athletes).containsExactly(0)
        }

        @Test
        fun `4 athletes climbing at rotation 16, returns 10, 12, 14`() {
            val athletes = Location.athletesResting(16, 4)
            assertThat(athletes).containsExactlyElementsIn(listOf(10, 12, 14)).inOrder()
        }

        @Test
        fun `2 athletes climbing at rotation 16, returns 14`() {
            val athletes = Location.athletesResting(16, 2)
            assertThat(athletes).containsExactly(14)
        }

        @ParameterizedTest
        @ValueSource(ints = [-11, -2, 1, 3, 5, 19, 23])
        fun `0 athletes climbing, returns an empty list`(rotation: Int) {
            val athletes = Location.athletesClimbing(rotation, 0)
            assertThat(athletes).isEmpty()
        }

    }

    @Nested
    @DisplayName("fistAthleteInIsolation method")
    inner class FistAthleteInIsolation {

        @Test
        fun `1 athlete in buffer at rotation -1, returns 1`() {
            val athlete = Location.firstAthleteInIsolation(-1, 1)
            assertThat(athlete).isEqualTo(1)
        }

        @Test
        fun `2 athletes in buffer at rotation -1, returns 2`() {
            val athlete = Location.firstAthleteInIsolation(-1, 2)
            assertThat(athlete).isEqualTo(2)
        }

        @Test
        fun `4 athletes in buffer at rotation 11, returns 16`() {
            val athlete = Location.firstAthleteInIsolation(11, 4)
            assertThat(athlete).isEqualTo(16)
        }

    }

    @Nested
    @DisplayName("athletesInIsolation method")
    inner class AthletesInIsolationMethod {

        @Test
        fun `first athlete in isolation is -3 and total participants 3, returns -3 until 3`() {
            val athletes = Location.athletesInIsolation(-3, 3)
            assertThat(athletes).containsExactlyElementsIn(listOf(-3, -2, -1, 0, 1, 2))
        }

        @Test
        fun `first athlete in isolation is 6 and total participants 12, returns 6 until 12`() {
            val athletes = Location.athletesInIsolation(6, 12)
            assertThat(athletes).containsExactlyElementsIn(listOf(6, 7, 8, 9, 10, 11)).inOrder()
        }

        @Test
        fun `first athlete in isolation is 4 and total participants 5, returns 4`() {
            val athletes = Location.athletesInIsolation(4, 5)
            assertThat(athletes).containsExactly(4)
        }

        @Test
        fun `first athlete in isolation is 8 and total participants 8, returns an empty list`() {
            val athletes = Location.athletesInIsolation(8, 8)
            assertThat(athletes).isEmpty()
        }

        @Test
        fun `first athlete in isolation is 13 and total participants 4, returns an empty list`() {
            val athletes = Location.athletesInIsolation(13, 4)
            assertThat(athletes).isEmpty()
        }

        @Test
        fun `first athlete in isolation is 3 and total participants -3, returns an empty list`() {
            val athletes = Location.athletesInIsolation(3, -3)
            assertThat(athletes).isEmpty()
        }

    }

    @Nested
    @DisplayName("athletesMoving method")
    inner class AthletesMovingMethod {

        @Test
        fun `1 athlete in buffer at rotation at rotation 5, returns 6`() {
            val athletes = Location.athleteMoving(5, 1)
            assertThat(athletes).isEqualTo(6)
        }

        @Test
        fun `2 athletes in buffer at at rotation -2, returns 0`() {
            val athletes = Location.athleteMoving(-2, 2)
            assertThat(athletes).isEqualTo(0)
        }

        @Test
        fun `2 athletes in buffer at rotation 14, returns 16`() {
            val athletes = Location.athleteMoving(14, 2)
            assertThat(athletes).isEqualTo(16)
        }

        @Test
        fun `0 athletes in buffer at rotation 11, returns 11`() {
            val athletes = Location.athleteMoving(11, 0)
            assertThat(athletes).isEqualTo(11)
        }

    }

    @Nested
    @DisplayName("athletesInBufferMethod method")
    inner class AthletesInBufferMethod {

        @Test
        fun `1 athlete in buffer at rotation 0, returns 0`() {
            val athletes = Location.athletesInBuffer(0, 1)
            assertThat(athletes).containsExactly(0)
        }

        @Test
        fun `1 athlete in buffer at rotation 5, returns 5`() {
            val athletes = Location.athletesInBuffer(5, 1)
            assertThat(athletes).containsExactly(5)
        }

        @Test
        fun `2 athletes in buffer at rotation 5, returns 5, 6`() {
            val athletes = Location.athletesInBuffer(5, 2)
            assertThat(athletes).containsExactlyElementsIn(listOf(5, 6)).inOrder()
        }

        @Test
        fun `3 athletes in buffer at rotation 12, returns 12, 13, 14`() {
            val athletes = Location.athletesInBuffer(12, 3)
            assertThat(athletes).containsExactlyElementsIn(listOf(12, 13, 14)).inOrder()
        }

        @ParameterizedTest
        @ValueSource(ints = [-11, -2, 1, 3, 5, 19, 23])
        fun `0 athletes in buffer, returns an empty list`(rotation: Int) {
            val athletes = Location.athletesInBuffer(rotation, 0)
            assertThat(athletes).isEmpty()
        }

    }

    @Nested
    @DisplayName("athletesInTransitZone method")
    inner class AthletesInTransitZoneMethod {

        @Test
        fun `5 athletes climbing and 1 athlete in buffer at rotation 4, returns -4, -2, 0, 2, 4`() {
            val athletes = Location.athletesInTransitZone(4, 5, 1)
            assertThat(athletes).containsExactlyElementsIn(listOf(-4, -2, 0, 2, 4)).inOrder()
        }

        @Test
        fun `2 athletes climbing and 1 athlete in buffer at rotation 13, returns 11, 13`() {
            val athletes = Location.athletesInTransitZone(13, 2, 1)
            assertThat(athletes).containsExactlyElementsIn(listOf(11, 13)).inOrder()
        }

        @Test
        fun `2 athletes climbing and 2 athlete in buffer at rotation 1, returns -3, -1, 1, 2`() {
            val athletes = Location.athletesInTransitZone(1, 2, 2)
            assertThat(athletes).containsExactlyElementsIn(listOf(-1, 1, 2)).inOrder()
        }

        @ParameterizedTest
        @ValueSource(ints = [-11, -2, 1, 3, 5, 19, 23])
        fun `2 athletes in buffer and 3 athletes climbing, returns an empty list`(rotation: Int) {
            val inTransitZone = Location.athletesResting(rotation, 2).toMutableList()
            inTransitZone.addAll(Location.athletesInBuffer(rotation, 3))

            val athletes = Location.athletesInTransitZone(rotation, 2, 3)
            assertThat(athletes).containsExactlyElementsIn(inTransitZone).inOrder()
        }

        @ParameterizedTest
        @ValueSource(ints = [-11, -2, 1, 3, 5, 19, 23])
        fun `0 athletes in buffer and 0 athletes climbing, returns an empty list`(rotation: Int) {
            val athletes = Location.athletesInTransitZone(rotation, 0, 0)
            assertThat(athletes).isEmpty()
        }

        @ParameterizedTest
        @ValueSource(ints = [-11, -2, 1, 3, 5, 19, 23])
        fun `0 athletes in buffer and 1 athletes climbing, returns an empty list`(rotation: Int) {
            val athletes = Location.athletesInTransitZone(rotation, 1, 0)
            assertThat(athletes).isEmpty()
        }
    }

    @Nested
    @DisplayName("isValidAthlete method")
    inner class IsValidAthleteMethod {

        @ParameterizedTest
        @ValueSource(ints = [1, 3, 5, 10, 25])
        fun `independent of total participants -1 athlete, returns false`(totalParticipants: Int) {
            val valid = Location.isValidAthlete(-1, totalParticipants)
            assertThat(valid).isFalse()
        }

        @ParameterizedTest
        @ValueSource(ints = [1, 3, 5, 10, 25])
        fun `independent of total participants -13 athlete, returns false`(totalParticipants: Int) {
            val valid = Location.isValidAthlete(-13, totalParticipants)
            assertThat(valid).isFalse()
        }

        @ParameterizedTest
        @ValueSource(ints = [0, 1, 3, 5, 10])
        fun `athletes from 0 until 20, returns true`(athlete: Int) {
            val valid = Location.isValidAthlete(athlete, 20)
            assertThat(valid).isTrue()
        }

        @ParameterizedTest
        @ValueSource(ints = [-13, -2, -5, 0])
        fun `athlete 0 and negative total participants parameter is treated same as 0`(totalParticipants: Int) {
            val valid = Location.isValidAthlete(0, totalParticipants)
            assertThat(valid).isFalse()
        }

        @ParameterizedTest
        @ValueSource(ints = [-13, -2, -5, 0])
        fun `athlete 15 and negative total participants parameter is treated same as 0`(totalParticipants: Int) {
            val valid = Location.isValidAthlete(15, totalParticipants)
            assertThat(valid).isFalse()
        }

        @ParameterizedTest
        @ValueSource(ints = [10, 25, 30])
        fun `athletes bigger (or equal) to 10 with total participants 10, returns false`(athlete: Int) {
            val valid = Location.isValidAthlete(athlete, 10)
            assertThat(valid).isFalse()
        }

    }

    @Nested
    @DisplayName("extractValidAthletes method")
    inner class ExtractValidAthletesMethod {

        @ParameterizedTest
        @ValueSource(ints = [1, 3, 5, 10, 25])
        fun `independent of total participants negative athletes, returns an empty list`(totalParticipants: Int) {
            val athletes = Location.extractValidAthletes(listOf(-12, -1, -3, -19), totalParticipants)
            assertThat(athletes).isEmpty()
        }

        @ParameterizedTest
        @ValueSource(ints = [1, 3, 5, 10, 25])
        fun `returns only athletes smaller then total participants`(totalParticipants: Int) {
            val athletes = Location.extractValidAthletes(listOf(0, 13, 2, 14, 22, 30), totalParticipants)
            for (athlete in athletes) {
                assertThat(athlete).isLessThan(totalParticipants)
            }
        }

        @ParameterizedTest
        @ValueSource(ints = [-13, -2, -5, 0])
        fun `negative total participants parameter is treated same as 0`(totalParticipants: Int) {
            val athletes = Location.extractValidAthletes(listOf(0, 13, 2, 14, 22, 30), totalParticipants)
            assertThat(athletes).isEmpty()
        }

        @Test
        fun `extract athletes from -1, 0, 3, 5, 6, 30 with 20 total participants, returns 0, 3, 5, 6`() {
            val athletes = Location.extractValidAthletes(listOf(-1, 0, 3, 5, 6, 30), 20)
            assertThat(athletes).containsExactlyElementsIn(listOf(0, 3, 5, 6)).inOrder()
        }

    }

    @Nested
    @DisplayName("athleteOnBoulder method")
    inner class AthleteOnBoulderMethod {

        @ParameterizedTest
        @ValueSource(ints = [6, 4, 2, 0])
        fun `at rotation 7, 5 athletes climbing, athletes 6, 4, 2, 0 are on a boulder`(athlete: Int) {
            val boulder = Location.athleteOnBoulder(7, athlete, 5)
            assertThat(boulder).isGreaterThan(0)
        }

        @ParameterizedTest
        @ValueSource(ints = [-1, -2, -3, -4, -10, -20])
        fun `negative athletes return zero at rotation 4, with 4 athletes climbing`(athlete: Int) {
            val boulder = Location.athleteOnBoulder(4, athlete, 4)
            assertThat(boulder).isEqualTo(0)
        }

        @ParameterizedTest
        @ValueSource(ints = [10, 5, 3, 1, -1, -30])
        fun `at rotation 7, 5 athletes climbing, athletes 10, 5, 3, 1, -1, -30 are not on a boulder`(athlete: Int) {
            val boulder = Location.athleteOnBoulder(7, athlete, 5)
            assertThat(boulder).isEqualTo(0)
        }

        @Test
        fun `at rotation 10, 3 athletes climbing, athlete 7 is on a boulder`() {
            val boulder = Location.athleteOnBoulder(10, 7, 3)
            assertThat(boulder).isEqualTo(2)
        }

        @Test
        fun `at rotation 7, 5 athletes climbing, athlete 0 is on boulder 4`() {
            val boulder = Location.athleteOnBoulder(7, 0, 5)
            assertThat(boulder).isEqualTo(4)
        }
    }
    
}