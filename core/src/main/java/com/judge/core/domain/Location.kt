package com.judge.core.domain

object Location {

    fun athletesClimbing(rotation: Int, athletesClimbing: Int): List<Int> {
        val athletes = mutableListOf<Int>()
        for (i in 0 until athletesClimbing) {
            athletes.add(rotation - 2*athletesClimbing + 1 + i*2)
        }
        return athletes
    }

    fun athletesResting(rotation: Int, athletesClimbing: Int): List<Int> {
        val athletes = mutableListOf<Int>()
        for (i in 1 until athletesClimbing) {
            athletes.add(rotation - 2*athletesClimbing + 2*i)
        }
        return athletes
    }

    fun firstAthleteInIsolation(rotation: Int, athletesInBuffer: Int): Int {
        return rotation + athletesInBuffer + 1
    }

    fun athletesInIsolation(firstAthleteInIsolation: Int, totalParticipants: Int): List<Int> {
        return (firstAthleteInIsolation until totalParticipants).toList()
    }

    fun athleteMoving(rotation: Int, athletesInBuffer: Int): Int {
        return rotation + athletesInBuffer
    }

    fun athletesInBuffer(rotation: Int, athletesInBuffer: Int): List<Int> {
        val athletes = mutableListOf<Int>()
        for (i in 0 until athletesInBuffer) {
            athletes.add(rotation + i)
        }
        return athletes
    }

    fun athletesInTransitZone(
            rotation: Int, athletesClimbing: Int, athletesInBuffer: Int): List<Int> {
        val athletes = athletesResting(rotation, athletesClimbing).toMutableList()
        athletes.addAll(athletesInBuffer(rotation, athletesInBuffer))
        return athletes
    }

    fun isValidAthlete(athlete: Int, totalParticipants: Int) : Boolean {
        val total = maxOf(totalParticipants, 0)
        return athlete in 0 until total
    }

    fun extractValidAthletes(athletes: List<Int>, totalParticipants: Int): List<Int> {
        return athletes.filter { athlete -> isValidAthlete(athlete, totalParticipants) }
    }

}
