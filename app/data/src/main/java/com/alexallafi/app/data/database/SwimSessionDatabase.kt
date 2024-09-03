package com.alexallafi.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SwimSession::class, SwimmingRound::class], version = 1)
abstract class SwimDatabase : RoomDatabase() {
    abstract fun swimSessionDao(): SwimSessionDao
}