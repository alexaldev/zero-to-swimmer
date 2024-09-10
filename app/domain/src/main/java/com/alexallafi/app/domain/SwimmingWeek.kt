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
        val SECOND = SwimmingWeek(2)
        val THIRD = SwimmingWeek(3)
        val FOURTH = SwimmingWeek(4)
        val FIFTH = SwimmingWeek(5)
        val SIXTH = SwimmingWeek(6)
    }
}