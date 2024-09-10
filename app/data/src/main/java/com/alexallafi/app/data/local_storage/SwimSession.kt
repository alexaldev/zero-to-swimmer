package com.alexallafi.app.data.local_storage

data class SwimSession(
    val priority: Int,
    val completed: Boolean,
    val week: Int,
    val swimSets: List<SwimmingSet>
)