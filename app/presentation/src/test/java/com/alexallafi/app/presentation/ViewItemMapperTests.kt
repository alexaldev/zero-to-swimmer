package com.alexallafi.app.presentation

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import com.alexallafi.app.domain.ConfigurationRepository
import com.alexallafi.app.domain.PoolSize
import com.alexallafi.app.domain.SessionGroup
import com.alexallafi.app.domain.SwimSession
import com.alexallafi.app.domain.SwimSessionsRepository
import com.alexallafi.app.domain.SwimmingSet
import com.alexallafi.app.domain.SwimmingWeek
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime

class ViewItemMapperTests {
    private lateinit var testMapper: ViewItemsMapper
    private val stringResourcesProvider: StringResourcesProvider = FakeStringResourcesProvider()

    @BeforeEach
    fun setup() {
        testMapper =
            ViewItemsMapper(
                stringResourcesProvider,
                includeOverview = false,
            )
    }

    @Test
    fun `sessions are grouped by their week and there is a header between each week's sessions`() =
        runTest {
            val sessions = fakeSwimSessionsValidSets()
            val groups = mapToGroups(sessions)

            val result =
                testMapper.mapToViewItems(
                    groups,
                    poolSize = PoolSize.Meters25,
                    favoriteId = "irrelevant",
                )

            assertThat(
                result.filterIsInstance<com.alexallafi.app.presentation.trainingProgram.SwimSessionListItem.WeekHeaderItem>(),
            ).isNotEmpty()
        }

    @Test
    fun `completed text for a week header is correct`() =
        runTest {
            val sessions = fakeSwimSessionsValidSets()
            val groups = mapToGroups(sessions)

            val firstWeekExpectedWeekHeaderItem =
                _root_ide_package_.com.alexallafi.app.presentation.trainingProgram.SwimSessionListItem.WeekHeaderItem(
                    startText = "Week 1",
                    endText = "[600m/3600m] Completed",
                )

            val testResult =
                testMapper.mapToViewItems(
                    groups,
                    poolSize = PoolSize.Meters25,
                    favoriteId = "irrelevant",
                )

            assertThat(testResult).contains(firstWeekExpectedWeekHeaderItem)
        }

    @Test
    fun `with a pool size of 25, a swim set view item can contain swim sets of 25 meters`() =
        runTest {
            val fakeSwimSet =
                SwimmingSet(
                    meters = 25,
                    count = 2,
                    restBreathsCount = 5,
                )
            val fakeSwimSession =
                SwimSession(
                    weekPriority = 1,
                    completed = false,
                    week = SwimmingWeek.FIRST,
                    swimSets = listOf(fakeSwimSet),
                    completedAt = null,
                )

            val testSwimSetViewItem =
                testMapper.toSwimSessionViewItem(
                    fakeSwimSession,
                    poolSize = PoolSize.Meters25,
                    favoriteId = "irrelevant",
                )
            assertThat(testSwimSetViewItem.swimRounds).contains("2 x 25")
        }

    @Test
    fun `with a pool size of 50, a swim set view item can contain only sets of 50 meters`() =
        runTest {
            val fakeSwimSet =
                SwimmingSet(
                    meters = 25,
                    count = 2,
                    restBreathsCount = 5,
                )
            val fakeSwimSession =
                SwimSession(
                    weekPriority = 1,
                    completed = false,
                    week = SwimmingWeek.FIRST,
                    swimSets = listOf(fakeSwimSet),
                    completedAt = null,
                )

            val testSwimSetViewItem =
                testMapper.toSwimSessionViewItem(
                    fakeSwimSession,
                    poolSize = PoolSize.Meters50,
                    favoriteId = "irrelevant",
                )
            assertThat(testSwimSetViewItem.swimRounds).contains("1 x 50")
        }

    private fun mapToGroups(sessions: List<SwimSession>): List<SessionGroup> =
        sessions
            .groupBy { it.week }
            .map { (week, weekSessions) ->
                SessionGroup(
                    week = week,
                    sessions = weekSessions,
                    completedMeters = weekSessions.filter { it.completed }.sumOf { it.swimSets.sumOf { set -> set.meters * set.count } },
                    totalMeters = weekSessions.sumOf { it.swimSets.sumOf { set -> set.meters * set.count } },
                )
            }.sortedBy { it.week.value }

    private fun fakeSwimSessionsValidSets(): List<SwimSession> =
        listOf(
            SwimSession(
                weekPriority = 1,
                completed = true,
                week = SwimmingWeek.FIRST,
                swimSets = buildList { repeat(1) { add(fakeSwimSet()) } },
                completedAt = OffsetDateTime.now(),
            ),
            SwimSession(
                weekPriority = 2,
                completed = false,
                week = SwimmingWeek.FIRST,
                swimSets = buildList { repeat(2) { add(fakeSwimSet()) } },
                completedAt = null,
            ),
            SwimSession(
                weekPriority = 1,
                completed = false,
                week = SwimmingWeek.SECOND,
                swimSets = buildList { repeat(4) { add(fakeSwimSet()) } },
                completedAt = null,
            ),
            SwimSession(
                weekPriority = 1,
                completed = false,
                week = SwimmingWeek.THIRD,
                swimSets = buildList { repeat(6) { add(fakeSwimSet()) } },
                completedAt = null,
            ),
            SwimSession(
                weekPriority = 3,
                completed = false,
                week = SwimmingWeek.FIRST,
                swimSets = buildList { repeat(3) { add(fakeSwimSet()) } },
                completedAt = null,
            ),
        )

    private fun fakeSwimSet() = SwimmingSet(meters = 300, count = 2, restBreathsCount = 10)
}
