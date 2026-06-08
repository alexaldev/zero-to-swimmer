package com.alexallafi.app.data.local_storage

import android.content.Context
import assertk.Assert
import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import assertk.assertions.prop
import com.alexallafi.app.domain.SwimSession
import com.alexallafi.app.domain.SwimmingSet
import com.alexallafi.app.domain.SwimmingWeek
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.time.OffsetDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class LocalSwimSessionsRepositoryTest {
    private lateinit var testRepository: LocalSwimSessionsRepository
    private val testDispatcher = UnconfinedTestDispatcher()
    private val context = mockk<Context>()

    @TempDir
    lateinit var tempDir: File

    @BeforeEach
    fun setup() {
        every { context.filesDir } returns tempDir
        testRepository =
            LocalSwimSessionsRepository(
                context = context,
                testDispatcher,
            )
    }

    private val fakeSwimSets =
        buildList {
            add(SwimmingSet(meters = 100, count = 4, restBreathsCount = 12))
            add(SwimmingSet(meters = 100, count = 4, restBreathsCount = 12))
            add(SwimmingSet(meters = 100, count = 4, restBreathsCount = 12))
        }

    @Test
    fun `addAll and then get should return the same sessions`() =
        runTest {
            val fakeSessions =
                buildList<SwimSession> {
                    add(SwimSession(1, true, SwimmingWeek.FIRST, fakeSwimSets, null))
                    add(SwimSession(2, false, SwimmingWeek.FIRST, fakeSwimSets, null))
                    add(SwimSession(3, false, SwimmingWeek.FIRST, fakeSwimSets, null))
                }
            testRepository.addAll(fakeSessions)

            val testSessions = testRepository.getAll()

            assertThat(testSessions.isSuccess).isTrue()
            assertThat(testSessions.getOrNull()).isNotNull()
            assertThat(testSessions.getOrNull()!!).isEqualTo(fakeSessions)
        }

    @Test
    fun `getAll should return sessions sorted by total priority`() =
        runTest {
            val fakeSessions =
                buildList {
                    add(SwimSession(1, true, SwimmingWeek.FIRST, fakeSwimSets, null))
                    add(SwimSession(1, false, SwimmingWeek.SECOND, fakeSwimSets, null))
                    add(SwimSession(3, false, SwimmingWeek.SECOND, fakeSwimSets, null))
                    add(SwimSession(2, false, SwimmingWeek.SECOND, fakeSwimSets, null))
                    add(SwimSession(2, false, SwimmingWeek.FIRST, fakeSwimSets, null))
                    add(SwimSession(3, false, SwimmingWeek.FIRST, fakeSwimSets, null))
                }
            testRepository.addAll(fakeSessions)

            val testSessions = testRepository.getAll().getOrThrow()

            assertThat(testSessions).isEqualTo(fakeSessions.sortedBy { it.totalPriority })
        }

    @Test
    fun `toggleCompleted should change completion status and set completion date`() =
        runTest {
            val fakeSecondSession = SwimSession(2, false, SwimmingWeek.FIRST, fakeSwimSets, null)
            val fakeSessions =
                listOf(
                    SwimSession(1, true, SwimmingWeek.FIRST, fakeSwimSets, null),
                    fakeSecondSession,
                    SwimSession(3, false, SwimmingWeek.FIRST, fakeSwimSets, null),
                    SwimSession(2, false, SwimmingWeek.SECOND, fakeSwimSets, null),
                )
            testRepository.addAll(fakeSessions)
            testRepository.toggleCompleted(fakeSecondSession)

            val testSecondStoredSession =
                testRepository.getAll().getOrThrow().first { it.id == fakeSecondSession.id }

            assertThat(testSecondStoredSession.completed).isTrue()
            assertThat(testSecondStoredSession.completedAt).isNotNull()

            testRepository.toggleCompleted(testSecondStoredSession)
            val toggledBackSession = testRepository.getAll().getOrThrow().first { it.id == fakeSecondSession.id }
            assertThat(toggledBackSession.completed).isFalse()
        }

    @Test
    fun `totalMetersForWeek should calculate correctly for specific week`() =
        runTest {
            val fakeSessions =
                listOf(
                    SwimSession(1, true, SwimmingWeek.FIRST, fakeSwimSets, null),
                    SwimSession(2, false, SwimmingWeek.FIRST, fakeSwimSets, null),
                    SwimSession(3, false, SwimmingWeek.SECOND, fakeSwimSets, null),
                )
            testRepository.addAll(fakeSessions)

            val testFirstWeekTotalMeters = testRepository.totalMetersForWeek(SwimmingWeek.FIRST)

            assertThat(testFirstWeekTotalMeters).isEqualTo(1200 * 2)
        }

    @Test
    fun `completedMetersForWeek should only count completed sessions for specific week`() =
        runTest {
            val fakeSessions =
                listOf(
                    SwimSession(1, true, SwimmingWeek.FIRST, fakeSwimSets, null),
                    SwimSession(2, false, SwimmingWeek.FIRST, fakeSwimSets, null),
                    SwimSession(3, false, SwimmingWeek.SECOND, fakeSwimSets, null),
                )
            testRepository.addAll(fakeSessions)

            val testFirstWeekCompletedMeters = testRepository.completedMetersForWeek(SwimmingWeek.FIRST)

            assertThat(testFirstWeekCompletedMeters).isEqualTo(1200)
        }

    @Test
    fun `isWeekCompleted should return true only when all sessions in week are completed`() =
        runTest {
            val fakeSessions =
                listOf(
                    SwimSession(1, true, SwimmingWeek.FIRST, fakeSwimSets, null),
                    SwimSession(2, true, SwimmingWeek.FIRST, fakeSwimSets, null),
                    SwimSession(3, false, SwimmingWeek.SECOND, fakeSwimSets, null),
                )
            testRepository.addAll(fakeSessions)

            assertThat(testRepository.isWeekCompleted(SwimmingWeek.FIRST)).isTrue()
            assertThat(testRepository.isWeekCompleted(SwimmingWeek.SECOND)).isFalse()
        }

    @Test
    fun `clearAll should clear completedAt field for all sessions and set as incomplete`() =
        runTest {
            val fakeSessions =
                listOf(
                    SwimSession(1, true, SwimmingWeek.FIRST, fakeSwimSets, OffsetDateTime.now()),
                    SwimSession(2, true, SwimmingWeek.FIRST, fakeSwimSets, OffsetDateTime.now()),
                    SwimSession(3, false, SwimmingWeek.SECOND, fakeSwimSets, null),
                )
            testRepository.addAll(fakeSessions)

            testRepository.clearAll()

            val result = testRepository.getAll()
            assertThat(result.isSuccess).isTrue()
            result
                .onSuccess { sessions ->
                    sessions.forEach { session -> assertThat(session).isIncomplete() }
                }.onFailure { fail("Failed to get all sessions") }
        }

    @Test
    fun `getById should return correct session or null if not found`() =
        runTest {
            val session1 = SwimSession(1, true, SwimmingWeek.FIRST, fakeSwimSets, null)
            val session2 = SwimSession(2, false, SwimmingWeek.FIRST, fakeSwimSets, null)
            testRepository.addAll(listOf(session1, session2))

            val result = testRepository.getById(session1.id)
            assertThat(result).isEqualTo(session1)

            val nullResult = testRepository.getById("non-existent")
            assertThat(nullResult).isEqualTo(null)
        }

    private fun Assert<SwimSession>.isIncomplete() {
        all {
            prop(SwimSession::completed).isEqualTo(false)
            prop(SwimSession::completedAt).isNull()
        }
    }
}
