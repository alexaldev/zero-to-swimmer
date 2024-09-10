package com.alexallafi.app.domain

data class SwimSession(
    val priority: Int,
    val completed: Boolean,
    val week: SwimmingWeek,
    val swimSets: List<SwimmingSet>
)
