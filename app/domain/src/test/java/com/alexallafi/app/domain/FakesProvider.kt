package com.alexallafi.app.domain

object FakesProvider {
    val fakeSession = SwimSession(1, false, SwimmingWeek.FIRST, emptyList(), null)

    val fakeSessions = List(26) { fakeSession }
}
