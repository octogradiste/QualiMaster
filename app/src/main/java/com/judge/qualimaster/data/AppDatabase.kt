package com.judge.qualimaster.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.judge.qualimaster.data.entities.AthleteEntity
import com.judge.qualimaster.data.entities.CategoryEntity
import com.judge.qualimaster.data.entities.CompetitionEntity
import com.judge.qualimaster.util.Constants.DATABASE_NAME

@Database(
    entities = [AthleteEntity::class, CategoryEntity::class, CompetitionEntity::class],
    version = 9
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun athleteDao(): AthleteDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        }
    }
}