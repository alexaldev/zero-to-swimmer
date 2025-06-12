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
                swimSets = buildList {
                    this += swimSet(12, 4, 100)
                    this += swimSet(8, 4, 50)
                    this += swimSet(4, 4, 25)
                }
                )
            )
            add(SwimSession(
                weekPriority = 2,
                completed = false,
                week = SwimmingWeek.FIRST,
                completedAt = null,
                swimSets = buildList {
                    this += swimSet(12, 4, 100)
                    this += swimSet(8, 4, 50)
                    this += swimSet(4, 4, 25)
                }
            )
            )
            add(SwimSession(
                weekPriority = 1,
                completed = false,
                week = SwimmingWeek.SECOND,
                completedAt = null,
                swimSets = buildList {
                    this += swimSet(12, 4, 100)
                    this += swimSet(8, 4, 50)
                    this += swimSet(4, 4, 25)
                }
            )
            )
        }
    }

    fun swimSet(restBreaths: Int, count: Int, meters: Int) = SwimmingSet(restBreathsCount = restBreaths, count = count, meters = meters)
}