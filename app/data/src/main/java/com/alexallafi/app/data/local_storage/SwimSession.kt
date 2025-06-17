package com.alexallafi.app.data.local_storage

import kotlinx.serialization.Serializable

@Serializable
data class SwimSession(
    val priority: Int,
    val completed: Boolean,
    val week: Int,
    val swimSets: List<SwimmingSet>,
    val completedAt: String?
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SwimSession) return false

        return this.priority == other.priority && this.week == other.week
    }

    override fun hashCode(): Int {
        var result = priority
        result = 31 * result + week.hashCode()
        return result
    }
}