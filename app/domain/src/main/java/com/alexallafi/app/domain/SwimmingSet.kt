package com.alexallafi.app.domain

/**
 * 4 x 100 meters = SwimmingRound(100, 4, breaths)
 */
data class SwimmingSet(
    val meters: Int,
    val count: Int,
    val restBreathsCount: Int
)