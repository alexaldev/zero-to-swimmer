package com.alexallafi.app.data.local_storage

import kotlinx.serialization.Serializable

@Serializable
data class SwimSession(
    val priority: Int,
    val completed: Boolean,
    val week: Int,
    val swimSets: List<SwimmingSet>
)