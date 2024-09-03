package com.alexallafi.app.data.database

import androidx.room.Embedded
import androidx.room.Relation

data class SwimSessionWithRounds(
    @Embedded val swimSession: SwimSession,
    @Relation(
        parentColumn = "id",
        entityColumn = "swimSessionId"
    )
    val swimmingRounds: List<SwimmingRound>
)