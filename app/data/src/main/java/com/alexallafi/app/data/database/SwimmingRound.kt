package com.alexallafi.app.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SwimmingRound(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val meters: Int,
    val count: Int,
    val restBreathsCount: Int,
    val swimSessionId: Int // Foreign key reference
)