package com.alexallafi.app.domain

data class SessionGroup(
    val week: SwimmingWeek,
    val sessions: List<SwimSession>,
    val completedMeters: Int,
    val totalMeters: Int
)
