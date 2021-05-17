package com.judge.core.domain

object RotationPeriod {

    fun rotationFromCurrentTime(startTime: Long, currentTime: Long, minutesPerBoulder: Int): Int {
        if (startTime > currentTime) return 0
        return ((currentTime - startTime) / (60 * 1000 * minutesPerBoulder) + 1).toInt()
    }

    fun maxRotation(athletesClimbing: Int, totalParticipants: Int) : Int {
        return 2*athletesClimbing - 1 + totalParticipants - 1
    }

}