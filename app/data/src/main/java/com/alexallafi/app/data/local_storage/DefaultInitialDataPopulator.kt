package com.alexallafi.app.data.local_storage

import com.alexallafi.app.domain.InitialDataPopulator
import com.alexallafi.app.domain.SwimSession
import com.alexallafi.app.domain.SwimmingSet
import com.alexallafi.app.domain.SwimmingWeek

class DefaultInitialDataPopulator : InitialDataPopulator {
    override fun createSessions(): List<SwimSession> {
        return buildList {
            add(SwimSession(
                weekPriority = 1,
                completed = false,
                week = SwimmingWeek.FIRST,
                completedAt = null,
                swimSets = listOf(
                    SwimmingSet(
                        restBreathsCount = 12,
                        count = 4,
                        meters = 100
                    ),
                    SwimmingSet(
                        restBreathsCount = 8,
                        count = 4,
                        meters = 50
                    ),
                    SwimmingSet(
                        restBreathsCount = 4,
                        count = 4,
                        meters = 25
                    )
                )
            ))
        }
    }
}