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
    }
}