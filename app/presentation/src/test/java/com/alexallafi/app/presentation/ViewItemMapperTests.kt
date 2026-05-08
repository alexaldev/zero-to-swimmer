package com.alexallafi.app.presentation

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import com.alexallafi.app.domain.ConfigurationRepository
import com.alexallafi.app.domain.PoolSize
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
    private val mockSwimSessionsRepository = mockk<SwimSessionsRepository>()
    private val mockConfigurationRepository = mockk<ConfigurationRepository>()

    @BeforeEach
    fun setup() {
        coEvery { mockConfigurationRepository.getPoolSize() } returns PoolSize.Meters25

        testMapper =
            ViewItemsMapper(
                stringResourcesProvider,
                mockSwimSessionsRepository,
                mockConfigurationRepository,
                includeOverview = false,
            )

        coEvery { mockSwimSessionsRepository.completedMetersForWeek(SwimmingWeek(1)) } returns 600
        coEvery { mockSwimSessionsRepository.totalMetersForWeek(SwimmingWeek(1)) } returns 3600

        coEvery { mockSwimSessionsRepository.completedMetersForWeek(SwimmingWeek(2)) } returns 0
        coEvery { mockSwimSessionsRepository.totalMetersForWeek(SwimmingWeek(2)) } returns 2400

        coEvery { mockSwimSessionsRepository.completedMetersForWeek(SwimmingWeek(3)) } returns 0
        coEvery { mockSwimSessionsRepository.totalMetersForWeek(SwimmingWeek(3)) } returns 3000
    }

    @Test
    fun `sessions are grouped by their week and there is a header between each week's sessions`() =
        runTest {
            val fakeSessions = fakeSwimSessionsEmptySets()

            // Header for Week 1, week 1 sessions, header for week 2, etc.
            val expected =
                listOf<SwimSessionListItem>(
                    SwimSessionListItem.WeekHeaderItem(
                        startText = "Week 1",
                        endText = "[600m/${600 + 1200 + 1800}m] Completed",
                    ),
                    SwimSessionListItem.SwimSessionViewItem(
                        id = "${SwimmingWeek.FIRST}-1",
                        title = "Day 1",
                        message = testMapper.sessionsCompletedMessaged(fakeSessions.first()),
                        isCompleted = true,
                    ),
                    SwimSessionListItem.SwimSessionViewItem(
                        id = "${SwimmingWeek.FIRST}-2",
                        title = "Day 2",
                        message = testMapper.sessionsCompletedMessaged(fakeSessions[1]),
                    ),
                    SwimSessionListItem.SwimSessionViewItem(
                        id = "${SwimmingWeek.FIRST}-3",
                        title = "Day 3",
                        message = testMapper.sessionsCompletedMessaged(fakeSessions[2]),
                    ),
                    SwimSessionListItem.WeekHeaderItem(
                        startText = "Week 2",
                        endText = "[0m/2400m] Completed",
                    ),
                    SwimSessionListItem.SwimSessionViewItem(
                        id = "${SwimmingWeek.SECOND}-1",
                        title = "Day 1",
                        message = testMapper.sessionsCompletedMessaged(fakeSessions[3]),
                    ),
                    SwimSessionListItem.WeekHeaderItem(
                        startText = "Week 3",
                        endText = "[0m/3000m] Completed",
                    ),
                    SwimSessionListItem.SwimSessionViewItem(
                        id = "${SwimmingWeek.THIRD}-1",
                        title = "Day 1",
                        message = testMapper.sessionsCompletedMessaged(fakeSessions[4]),
                    ),
                )

            val result = testMapper.mapToViewItems(fakeSessions)

            assertThat(result).isEqualTo(expected)
        }

    @Test
    fun `completed text for a week header is correct`() =
        runTest {
            val fakeSessions = fakeSwimSessionsValidSets()

            val firstWeekExpectedWeekHeaderItem =
                SwimSessionListItem.WeekHeaderItem(
                    startText = "Week 1",
                    endText = "[600m/${600 + 1200 + 1800}m] Completed",
                )
            val secondWeekExpectedWeekHeaderItem =
                SwimSessionListItem.WeekHeaderItem(
                    startText = "Week 2",
                    endText = "[0m/2400m] Completed",
                )
            val thirdWeekExpectedWeekHeaderItem =
                SwimSessionListItem.WeekHeaderItem(
                    startText = "Week 3",
                    endText = "[0m/3000m] Completed",
                )

            val testResult = testMapper.mapToViewItems(fakeSessions)

            assertThat(testResult).contains(firstWeekExpectedWeekHeaderItem)
            assertThat(testResult).contains(secondWeekExpectedWeekHeaderItem)
            assertThat(testResult).contains(thirdWeekExpectedWeekHeaderItem)
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

            val testSwimSetViewItem = testMapper.toSwimSessionViewItem(fakeSwimSession)
            assertThat(testSwimSetViewItem.swimRounds).contains("2 x 25")
        }

    @Test
    fun `with a pool size of 50, a swim set view item can contain only sets of 50 meters`() =
        runTest {
            coEvery { mockConfigurationRepository.getPoolSize() } returns PoolSize.Meters50

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

            val testSwimSetViewItem = testMapper.toSwimSessionViewItem(fakeSwimSession)
            assertThat(testSwimSetViewItem.swimRounds).contains("1 x 50")
        }

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

    private fun fakeSwimSessionsEmptySets(): List<SwimSession> {
        val fakeSessions =
            listOf(
                SwimSession(
                    weekPriority = 1,
                    completed = true,
                    week = SwimmingWeek.FIRST,
                    swimSets = emptyList(),
                    completedAt = OffsetDateTime.now(),
                ),
                SwimSession(
                    weekPriority = 2,
                    completed = false,
                    week = SwimmingWeek.FIRST,
                    swimSets = emptyList(),
                    completedAt = null,
                ),
                SwimSession(
                    weekPriority = 1,
                    completed = false,
                    week = SwimmingWeek.SECOND,
                    swimSets = emptyList(),
                    completedAt = null,
                ),
                SwimSession(
                    weekPriority = 1,
                    completed = false,
                    week = SwimmingWeek.THIRD,
                    swimSets = emptyList(),
                    completedAt = null,
                ),
                SwimSession(
                    weekPriority = 3,
                    completed = false,
                    week = SwimmingWeek.FIRST,
                    swimSets = emptyList(),
                    completedAt = null,
                ),
            )
        return fakeSessions.sortedBy { it.totalPriority }
    }

    private fun fakeSwimSet() = SwimmingSet(meters = 300, count = 2, restBreathsCount = 10)
}
