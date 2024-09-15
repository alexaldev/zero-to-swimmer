package com.alexallafi.app.presentation

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import com.alexallafi.app.domain.SwimSession
import com.alexallafi.app.domain.SwimSessionsRepository
import com.alexallafi.app.domain.SwimmingSet
import com.alexallafi.app.domain.SwimmingWeek
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ViewItemMapperTests {

    private lateinit var testMapper: ViewItemsMapper
    private val stringResourcesProvider: StringResourcesProvider = FakeStringResourcesProvider()
    private val mockRepository = mockk<SwimSessionsRepository>()

    @BeforeEach
    fun setup() {

        testMapper = ViewItemsMapper(stringResourcesProvider, mockRepository)

        coEvery { mockRepository.completedMetersForWeek(SwimmingWeek(1)) } returns 600
        coEvery { mockRepository.totalMetersForWeek(SwimmingWeek(1)) } returns 3600

        coEvery { mockRepository.completedMetersForWeek(SwimmingWeek(2)) } returns 0
        coEvery { mockRepository.totalMetersForWeek(SwimmingWeek(2)) } returns 2400

        coEvery { mockRepository.completedMetersForWeek(SwimmingWeek(3)) } returns 0
        coEvery { mockRepository.totalMetersForWeek(SwimmingWeek(3)) } returns 3000
    }

    @Test
    fun `sessions are grouped by their week and there is a header between each week's sessions`() = runTest {
        val fakeSessions = fakeSwimSessionsEmptySets()

        // Header for Week 1, week 1 sessions, header for week 2, etc.
        val expected = listOf<SwimSessionListItem>(
            SwimSessionListItem.HeaderItem(
                startText = "Week 1",
                endText = "[600/${600 + 1200 + 1800}] Completed",
            ),
            SwimSessionListItem.SwimSessionViewItem(
                isCompleted = true
            ),
            SwimSessionListItem.SwimSessionViewItem(),
            SwimSessionListItem.SwimSessionViewItem(),
            SwimSessionListItem.HeaderItem(
                startText = "Week 2",
                endText = "[0/2400] Completed"
            ),
            SwimSessionListItem.SwimSessionViewItem(),
            SwimSessionListItem.HeaderItem(
                startText = "Week 3",
                endText = "[0/3000] Completed"
            ),
            SwimSessionListItem.SwimSessionViewItem(),
        )

        val result = testMapper.mapToViewItems(fakeSessions)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `completed text for a week header is correct`() = runTest {

        val fakeSessions = fakeSwimSessionsValidSets()

        val firstWeekExpectedHeaderItem = SwimSessionListItem.HeaderItem(
            startText = "Week 1",
            endText = "[600/${600 + 1200 + 1800}] Completed"
        )
        val secondWeekExpectedHeaderItem = SwimSessionListItem.HeaderItem(
            startText = "Week 2",
            endText = "[0/2400] Completed"
        )
        val thirdWeekExpectedHeaderItem = SwimSessionListItem.HeaderItem(
            startText = "Week 3",
            endText = "[0/3000] Completed"
        )

        val testResult = testMapper.mapToViewItems(fakeSessions)

        assertThat(testResult).contains(firstWeekExpectedHeaderItem)
        assertThat(testResult).contains(secondWeekExpectedHeaderItem)
        assertThat(testResult).contains(thirdWeekExpectedHeaderItem)
    }

    private fun fakeSwimSessionsValidSets(): List<SwimSession> {
        return listOf(
            SwimSession(
                priority = 1,
                completed = true,
                week = SwimmingWeek.FIRST,
                swimSets = buildList { repeat(1) { add(fakeSwimSet())} },
                completedAt = null
            ),
            SwimSession(
                priority = 2,
                completed = false,
                week = SwimmingWeek.FIRST,
                swimSets = buildList { repeat(2) { add(fakeSwimSet())} },
                completedAt = null
            ),
            SwimSession(
                priority = 4,
                completed = false,
                week = SwimmingWeek.SECOND,
                swimSets = buildList { repeat(4) { add(fakeSwimSet())} },
                completedAt = null
            ),
            SwimSession(
                priority = 5,
                completed = false,
                week = SwimmingWeek.THIRD,
                swimSets = buildList { repeat(6) { add(fakeSwimSet())} },
                completedAt = null
            ),
            SwimSession(
                priority = 3,
                completed = false,
                week = SwimmingWeek.FIRST,
                swimSets = buildList { repeat(3) { add(fakeSwimSet())} },
                completedAt = null
            )
        )
    }

    private fun fakeSwimSessionsEmptySets(): List<SwimSession> {
        val fakeSessions = listOf(
            SwimSession(
                priority = 1,
                completed = true,
                week = SwimmingWeek.FIRST,
                swimSets = emptyList(),
                completedAt = null
            ),
            SwimSession(
                priority = 2,
                completed = false,
                week = SwimmingWeek.FIRST,
                swimSets = emptyList(),
                completedAt = null
            ),
            SwimSession(
                priority = 4,
                completed = false,
                week = SwimmingWeek.SECOND,
                swimSets = emptyList(),
                completedAt = null
            ),
            SwimSession(
                priority = 5,
                completed = false,
                week = SwimmingWeek.THIRD,
                swimSets = emptyList(),
                completedAt = null
            ),
            SwimSession(
                priority = 3,
                completed = false,
                week = SwimmingWeek.FIRST,
                swimSets = emptyList(),
                completedAt = null
            )
        )
        return fakeSessions
    }

    private fun fakeSwimSet() = SwimmingSet(meters = 300, count = 2, restBreathsCount = 10)
}