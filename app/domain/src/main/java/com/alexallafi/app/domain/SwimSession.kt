package com.alexallafi.app.domain

import java.time.OffsetDateTime

data class SwimSession(
    val weekPriority: Int,
    val completed: Boolean,
    val week: SwimmingWeek,
    val swimSets: List<SwimmingSet>,
    val completedAt: OffsetDateTime?
) {

    val id: String
        get() {
            return "$week-$weekPriority"
        }

    val totalPriority: Int
        get() {
            return weekPriority + (AVAILABLE_WEEK_PRIORITIES.last * week.value)
        }

    init {
        require(weekPriority in AVAILABLE_WEEK_PRIORITIES) { "Priority must be in $AVAILABLE_WEEK_PRIORITIES" }
    }

    companion object {
        val AVAILABLE_WEEK_PRIORITIES = (1..3)
    }
}
