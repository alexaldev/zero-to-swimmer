package com.alexallafi.app.domain

@JvmInline
value class SwimmingWeek(
    val value: Int
) {
    init {
        require(value in PERMITTED_RANGE) { "Swimming week must be in $PERMITTED_RANGE" }
    }

    companion object {
        val PERMITTED_RANGE = 1..6

        val FIRST = SwimmingWeek(1)
        val SECOND = SwimmingWeek(1)
        val THIRD = SwimmingWeek(1)
        val FOURTH = SwimmingWeek(1)
        val FIFTH = SwimmingWeek(1)
        val SIXTH = SwimmingWeek(1)
    }
}