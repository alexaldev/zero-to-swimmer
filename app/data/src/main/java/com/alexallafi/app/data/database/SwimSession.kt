package com.alexallafi.app.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SwimSession(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val priority: Int,
    val completed: Boolean,
    val week: Int
)