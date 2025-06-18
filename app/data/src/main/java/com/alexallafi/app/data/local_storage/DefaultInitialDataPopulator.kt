package com.alexallafi.app.data.local_storage

import com.alexallafi.app.domain.InitialDataPopulator
import com.alexallafi.app.domain.SwimSession
import com.alexallafi.app.domain.SwimmingSet
import com.alexallafi.app.domain.SwimmingWeek

class DefaultInitialDataPopulator : InitialDataPopulator {
    override fun createSessions(): List<SwimSession> {
        return buildList {
            // WEEK 1 - Three Days
            add(
                SwimSession(
                    weekPriority = 1,
                    completed = false,
                    week = SwimmingWeek.FIRST,
                    completedAt = null,
                    swimSets = buildList {
                        add(swimSet(12, 4, 100))
                        add(swimSet(8, 4, 50))
                        add(swimSet(4, 4, 25))
                    }
                )
            )
            add(
                SwimSession(
                    weekPriority = 2,
                    completed = false,
                    week = SwimmingWeek.FIRST,
                    completedAt = null,
                    swimSets = buildList {
                        add(swimSet(12, 4, 100))
                        add(swimSet(8, 4, 50))
                        add(swimSet(4, 4, 25))
                    }
                )
            )
            add(
                SwimSession(
                    weekPriority = 3,
                    completed = false,
                    week = SwimmingWeek.FIRST,
                    completedAt = null,
                    swimSets = buildList {
                        add(swimSet(12, 4, 100))
                        add(swimSet(8, 4, 50))
                        add(swimSet(4, 4, 25))
                    }
                )
            )

            // WEEK 2
            add(
                SwimSession(
                    weekPriority = 1,
                    completed = false,
                    week = SwimmingWeek.SECOND,
                    completedAt = null,
                    swimSets = buildList {
                        add(swimSet(12, 1, 200))
                        add(swimSet(10, 4, 100))
                        add(swimSet(6, 4, 50))
                        add(swimSet(4, 4, 25))
                    }
                )
            )
            add(
                SwimSession(
                    weekPriority = 2,
                    completed = false,
                    week = SwimmingWeek.SECOND,
                    completedAt = null,
                    swimSets = buildList {
                        add(swimSet(12, 1, 200))
                        add(swimSet(10, 4, 100))
                        add(swimSet(6, 4, 50))
                        add(swimSet(4, 4, 25))
                    }
                )
            )
            add(
                SwimSession(
                    weekPriority = 3,
                    completed = false,
                    week = SwimmingWeek.SECOND,
                    completedAt = null,
                    swimSets = buildList {
                        add(swimSet(12, 1, 200))
                        add(swimSet(10, 4, 100))
                        add(swimSet(6, 4, 50))
                        add(swimSet(4, 4, 25))
                    }
                )
            )

            // WEEK 3
            add(
                SwimSession(
                    weekPriority = 1,
                    completed = false,
                    week = SwimmingWeek.THIRD,
                    completedAt = null,
                    swimSets = buildList {
                        add(swimSet(12, 1, 400))
                        add(swimSet(10, 1, 200))
                        add(swimSet(8, 4, 100))
                        add(swimSet(4, 4, 50))
                    }
                )
            )
            add(
                SwimSession(
                    weekPriority = 2,
                    completed = false,
                    week = SwimmingWeek.THIRD,
                    completedAt = null,
                    swimSets = buildList {
                        add(swimSet(12, 1, 400))
                        add(swimSet(10, 1, 200))
                        add(swimSet(8, 4, 100))
                        add(swimSet(4, 4, 50))
                    }
                )
            )
            add(
                SwimSession(
                    weekPriority = 3,
                    completed = false,
                    week = SwimmingWeek.THIRD,
                    completedAt = null,
                    swimSets = buildList {
                        add(swimSet(12, 1, 400))
                        add(swimSet(10, 1, 200))
                        add(swimSet(8, 4, 100))
                        add(swimSet(4, 4, 50))
                    }
                )
            )

            // WEEK 4
            add(
                SwimSession(
                    weekPriority = 1,
                    completed = false,
                    week = SwimmingWeek.FOURTH,
                    completedAt = null,
                    swimSets = buildList {
                        add(swimSet(10, 1, 600))
                        add(swimSet(8, 1, 300))
                        add(swimSet(6, 4, 100))
                        add(swimSet(4, 4, 50))
                    }
                )
            )
            add(
                SwimSession(
                    weekPriority = 2,
                    completed = false,
                    week = SwimmingWeek.FOURTH,
                    completedAt = null,
                    swimSets = buildList {
                        add(swimSet(10, 1, 600))
                        add(swimSet(8, 1, 300))
                        add(swimSet(6, 4, 100))
                        add(swimSet(4, 4, 50))
                    }
                )
            )
            add(
                SwimSession(
                    weekPriority = 3,
                    completed = false,
                    week = SwimmingWeek.FOURTH,
                    completedAt = null,
                    swimSets = buildList {
                        add(swimSet(10, 1, 600))
                        add(swimSet(8, 1, 300))
                        add(swimSet(6, 4, 100))
                        add(swimSet(4, 4, 50))
                    }
                )
            )

            // WEEK 5
            add(
                SwimSession(
                    weekPriority = 1,
                    completed = false,
                    week = SwimmingWeek.FIFTH,
                    completedAt = null,
                    swimSets = buildList {
                        add(swimSet(8, 1, 1000))
                        add(swimSet(4, 4, 100))
                        add(swimSet(4, 4, 50))
                    }
                )
            )
            add(
                SwimSession(
                    weekPriority = 2,
                    completed = false,
                    week = SwimmingWeek.FIFTH,
                    completedAt = null,
                    swimSets = buildList {
                        add(swimSet(8, 1, 1000))
                        add(swimSet(4, 4, 100))
                        add(swimSet(4, 4, 50))
                    }
                )
            )
            add(
                SwimSession(
                    weekPriority = 3,
                    completed = false,
                    week = SwimmingWeek.FIFTH,
                    completedAt = null,
                    swimSets = buildList {
                        add(swimSet(8, 1, 1000))
                        add(swimSet(4, 4, 100))
                        add(swimSet(4, 4, 50))
                    }
                )
            )


            // WEEK 6 - Day 1
            add(
                SwimSession(
                    weekPriority = 1,
                    completed = false,
                    week = SwimmingWeek.SIXTH,
                    completedAt = null,
                    swimSets = buildList {
                        add(swimSet(6, 1, 1200))
                        add(swimSet(4, 3, 100))
                        add(swimSet(4, 3, 50))
                    }
                )
            )

            // WEEK 6 - Day 2
            add(
                SwimSession(
                    weekPriority = 2,
                    completed = false,
                    week = SwimmingWeek.SIXTH,
                    completedAt = null,
                    swimSets = buildList {
                        add(swimSet(6, 1, 1200))
                        add(swimSet(4, 3, 100))
                        add(swimSet(4, 3, 50))
                    }
                )
            )

            // WEEK 6 - Day 3 (straight swim)
            add(
                SwimSession(
                    weekPriority = 3,
                    completed = false,
                    week = SwimmingWeek.SIXTH,
                    completedAt = null,
                    swimSets = buildList {
                        add(swimSet(0, 1, 1650)) // No rest between sets, straight swim
                    }
                )
            )
        }
    }

    fun swimSet(restBreaths: Int, count: Int, meters: Int) = SwimmingSet(restBreathsCount = restBreaths, count = count, meters = meters)
}