package com.alexallafi.app.data.local_storage

import kotlinx.serialization.Serializable

@Serializable
data class SwimmingSet(
    val meters: Int,
    val count: Int,
    val restBreathsCount: Int,
)