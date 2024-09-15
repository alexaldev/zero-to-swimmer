package com.alexallafi.app.domain

import java.time.OffsetDateTime

data class SwimSession(
    val priority: Int,
    val completed: Boolean,
    val week: SwimmingWeek,
    val swimSets: List<SwimmingSet>,
    val completedAt: OffsetDateTime?
)
