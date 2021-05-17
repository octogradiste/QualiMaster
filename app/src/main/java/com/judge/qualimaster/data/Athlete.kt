package com.judge.qualimaster.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "athletes")
data class Athlete(
    @PrimaryKey() var athleteNumber: Int,
    var startNumber: Int,
    var firstName: String,
    var lastName: String,
    var ageGroup: Int,
    var licence: Int
)