package com.alexallafi.app.data.local_storage

import androidx.test.core.app.ApplicationProvider
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import com.alexallafi.app.domain.SwimSession
import com.alexallafi.app.domain.SwimmingSet
import com.alexallafi.app.domain.SwimmingWeek
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LocalSwimSessionsRepositoryTests {

    private lateinit var testRepository: LocalSwimSessionsRepository
    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        testRepository = LocalSwimSessionsRepository(
            context = ApplicationProvider.getApplicationContext(),
            testDispatcher
        )
    }

    @AfterEach
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun addAllAndGet() = runTest {

        val fakeSwimSets = buildList<SwimmingSet> {
            add(
                SwimmingSet(
                    meters = 100,
                    count = 4,
                    restBreathsCount = 12
                )
            )
            add(
                SwimmingSet(
                    meters = 100,
                    count = 4,
                    restBreathsCount = 12
                )
            )
            add(
                SwimmingSet(
                    meters = 100,
                    count = 4,
                    restBreathsCount = 12
                )
            )
        }

        val fakeSessions = buildList<SwimSession> {
            add(
                SwimSession(
                    weekPriority = 1,
                    completed = true,
                    week = SwimmingWeek.FIRST,
                    swimSets = fakeSwimSets,
                    completedAt = null,
                )
            )
            add(
                SwimSession(
                    weekPriority = 2,
                    completed = false,
                    week = SwimmingWeek.FIRST,
                    swimSets = fakeSwimSets,
                    completedAt = null,
                )
            )
            add(
                SwimSession(
                    weekPriority = 3,
                    completed = false,
                    week = SwimmingWeek.FIRST,
                    swimSets = fakeSwimSets,
                    completedAt = null,
                )
            )
        }
        testRepository.addAll(fakeSessions)

        val testSessions = testRepository.getAll()

        assertThat(testSessions.isSuccess).isTrue()
        assertThat(testSessions.getOrNull()).isNotNull()
        assertThat(testSessions.getOrNull()!!).isEqualTo(fakeSessions)
    }

    @Test
    fun getAllSortedByTotalPriority() = runTest {

        val fakeSwimSets = buildList<SwimmingSet> {
            add(
                SwimmingSet(
                    meters = 100,
                    count = 4,
                    restBreathsCount = 12
                )
            )
            add(
                SwimmingSet(
                    meters = 100,
                    count = 4,
                    restBreathsCount = 12
                )
            )
            add(
                SwimmingSet(
                    meters = 100,
                    count = 4,
                    restBreathsCount = 12
                )
            )
        }

        val fakeSessions = buildList<SwimSession> {
            add(
                SwimSession(
                    weekPriority = 1,
                    completed = true,
                    week = SwimmingWeek.FIRST,
                    swimSets = fakeSwimSets,
                    completedAt = null,
                )
            )
            add(
                SwimSession(
                    weekPriority = 1,
                    completed = false,
                    week = SwimmingWeek.SECOND,
                    swimSets = fakeSwimSets,
                    completedAt = null,
                )
            )
            add(
                SwimSession(
                    weekPriority = 3,
                    completed = false,
                    week = SwimmingWeek.SECOND,
                    swimSets = fakeSwimSets,
                    completedAt = null,
                )
            )
            add(
                SwimSession(
                    weekPriority = 2,
                    completed = false,
                    week = SwimmingWeek.SECOND,
                    swimSets = fakeSwimSets,
                    completedAt = null,
                )
            )
            add(
                SwimSession(
                    weekPriority = 2,
                    completed = false,
                    week = SwimmingWeek.FIRST,
                    swimSets = fakeSwimSets,
                    completedAt = null,
                )
            )
            add(
                SwimSession(
                    weekPriority = 3,
                    completed = false,
                    week = SwimmingWeek.FIRST,
                    swimSets = fakeSwimSets,
                    completedAt = null,
                )
            )
        }
        testRepository.addAll(fakeSessions)

        val testSessions = testRepository.getAll().getOrThrow()

        assertThat(testSessions).isEqualTo(fakeSessions.sortedBy { it.totalPriority })
    }

    @Test
    fun markAsCompleted() = runTest {
        val fakeSwimSets = buildList<SwimmingSet> {
            add(
                SwimmingSet(
                    meters = 100,
                    count = 4,
                    restBreathsCount = 12
                )
            )
            add(
                SwimmingSet(
                    meters = 100,
                    count = 4,
                    restBreathsCount = 12
                )
            )
            add(
                SwimmingSet(
                    meters = 100,
                    count = 4,
                    restBreathsCount = 12
                )
            )
        }

        val fakeSecondSession = SwimSession(
            weekPriority = 2,
            completed = false,
            week = SwimmingWeek.FIRST,
            swimSets = fakeSwimSets,
            completedAt = null,
        )
        val fakeSessions = buildList {
            add(
                SwimSession(
                    weekPriority = 1,
                    completed = true,
                    week = SwimmingWeek.FIRST,
                    swimSets = fakeSwimSets,
                    completedAt = null,
                )
            )
            add(fakeSecondSession)
            add(
                SwimSession(
                    weekPriority = 3,
                    completed = false,
                    week = SwimmingWeek.FIRST,
                    swimSets = fakeSwimSets,
                    completedAt = null,
                )
            )
            add(
                SwimSession(
                    weekPriority = 2,
                    completed = false,
                    week = SwimmingWeek.SECOND,
                    swimSets = fakeSwimSets,
                    completedAt = null,
                )
            )
        }
        testRepository.addAll(fakeSessions)
        testRepository.markAsCompleted(fakeSecondSession)

        val testSecondStoredSession =
            testRepository.getAll().getOrThrow().first { it.weekPriority == fakeSecondSession.weekPriority }

        assertThat(testSecondStoredSession.completed).isTrue()
    }

    @Test
    fun totalMetersForWeek() = runTest {

        val fakeSwimSets = buildList<SwimmingSet> {
            add(
                SwimmingSet(
                    meters = 100,
                    count = 4,
                    restBreathsCount = 12
                )
            )
            add(
                SwimmingSet(
                    meters = 100,
                    count = 4,
                    restBreathsCount = 12
                )
            )
            add(
                SwimmingSet(
                    meters = 100,
                    count = 4,
                    restBreathsCount = 12
                )
            )
        }

        val fakeSecondSession = SwimSession(
            weekPriority = 2,
            completed = false,
            week = SwimmingWeek.FIRST,
            swimSets = fakeSwimSets,
            completedAt = null,
        )
        val fakeSessions = buildList {
            add(
                SwimSession(
                    weekPriority = 1,
                    completed = true,
                    week = SwimmingWeek.FIRST,
                    swimSets = fakeSwimSets,
                    completedAt = null,
                )
            )
            add(fakeSecondSession)
            add(
                SwimSession(
                    weekPriority = 3,
                    completed = false,
                    week = SwimmingWeek.SECOND,
                    swimSets = fakeSwimSets,
                    completedAt = null,
                )
            )
        }
        testRepository.addAll(fakeSessions)

        val testFirstWeekTotalMeters = testRepository.totalMetersForWeek(SwimmingWeek.FIRST)

        assertThat(testFirstWeekTotalMeters).isEqualTo(1200 * 2)
    }

    @Test
    fun completedMetersForWeek() = runTest {

        val fakeSwimSets = buildList<SwimmingSet> {
            add(
                SwimmingSet(
                    meters = 100,
                    count = 4,
                    restBreathsCount = 12
                )
            )
            add(
                SwimmingSet(
                    meters = 100,
                    count = 4,
                    restBreathsCount = 12
                )
            )
            add(
                SwimmingSet(
                    meters = 100,
                    count = 4,
                    restBreathsCount = 12
                )
            )
        }

        val fakeSessions = buildList {
            add(
                SwimSession(
                    weekPriority = 1,
                    completed = true,
                    week = SwimmingWeek.FIRST,
                    swimSets = fakeSwimSets,
                    completedAt = null,
                )
            )
            add(
                SwimSession(
                    weekPriority = 2,
                    completed = false,
                    week = SwimmingWeek.FIRST,
                    swimSets = fakeSwimSets,
                    completedAt = null,
                )
            )
            add(
                SwimSession(
                    weekPriority = 3,
                    completed = false,
                    week = SwimmingWeek.SECOND,
                    swimSets = fakeSwimSets,
                    completedAt = null,
                )
            )
        }
        testRepository.addAll(fakeSessions)

        val testFirstWeekCompletedMeters = testRepository.completedMetersForWeek(SwimmingWeek.FIRST)

        assertThat(testFirstWeekCompletedMeters).isEqualTo(1200)
    }

    @Test
    fun isWeekCompleted() = runTest {
        val fakeSwimSets = buildList<SwimmingSet> {
            add(
                SwimmingSet(
                    meters = 100,
                    count = 4,
                    restBreathsCount = 12
                )
            )
            add(
                SwimmingSet(
                    meters = 100,
                    count = 4,
                    restBreathsCount = 12
                )
            )
            add(
                SwimmingSet(
                    meters = 100,
                    count = 4,
                    restBreathsCount = 12
                )
            )
        }

        val fakeSessions = buildList {
            add(
                SwimSession(
                    weekPriority = 1,
                    completed = true,
                    week = SwimmingWeek.FIRST,
                    swimSets = fakeSwimSets,
                    completedAt = null,
                )
            )
            add(
                SwimSession(
                    weekPriority = 2,
                    completed = true,
                    week = SwimmingWeek.FIRST,
                    swimSets = fakeSwimSets,
                    completedAt = null,
                )
            )
            add(
                SwimSession(
                    weekPriority = 3,
                    completed = false,
                    week = SwimmingWeek.SECOND,
                    swimSets = fakeSwimSets,
                    completedAt = null,
                )
            )
        }
        testRepository.addAll(fakeSessions)

        assertThat(testRepository.isWeekCompleted(SwimmingWeek.FIRST)).isTrue()
        assertThat(testRepository.isWeekCompleted(SwimmingWeek.SECOND)).isFalse()
    }
}