package com.judge.qualimaster.io

import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

//class CSVReader(private val inputStream: InputStream) {
//
//    private val TAG = "CSVReader"
//
//    fun getAthletes() : List<Athlete> {
//        val reader = BufferedReader(InputStreamReader(inputStream))
//        val athletes = mutableListOf<Athlete>()
//        try {
//            var line = reader.readLine()
//
//            val header = line.split(";")
//
//            if (header.size != 6) {
//                Log.e(TAG, "Unexpected number of columns : ${header.size}")
//            }
//
//            for (column in listOf("Startfolge", "Startnummer", "Name", "Vorname", "Jahrgang", "Lizenz")) {
//                if (column !in header) {
//                    Log.e(TAG, "Unexpected column : $column")
//                }
//            }
//
//            line = reader.readLine()
//
//            while (line != null) {
//                val row = line.split(";")
//
//                athletes.add(Athlete(
//                        row[1].toInt(),
//                        row[0].toInt(),
//                        row[3],
//                        row[2],
//                        row[4].toInt(),
//                        row[5].toInt()
//                ))
//
//                line = reader.readLine()
//            }
//        } catch (e: IOException) {
//            Log.e(TAG, "Error while reading the CSV file : ${e.message}")
//        } finally {
//            reader.close()
//        }
//        return athletes.toList()
//    }
//}