package com.alexallafi.app.data.local_storage

import android.content.SharedPreferences
import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import com.alexallafi.app.domain.SwimSession
import com.alexallafi.app.domain.SwimmingWeek
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime

class LocalHistoryRepositoryTest {
    private val prefs = mockk<SharedPreferences>(relaxed = true)
    private val editor = mockk<SharedPreferences.Editor>(relaxed = true)
    private lateinit var repository: LocalHistoryRepository

    @BeforeEach
    fun setup() {
        every { prefs.edit() } returns editor
        every { editor.putString(any(), any()) } returns editor
        // Initial state for historyStateFlow in LocalHistoryRepository
        every { prefs.getString("history", "") } returns ""
        repository = LocalHistoryRepository(prefs)
    }

    @Test
    fun `getAll returns empty list when no history stored`() =
        runTest {
            every { prefs.getString("history", "") } returns ""

            val result = repository.getAll()

            assertThat(result).isEmpty()
        }

    @Test
    fun `getAll returns sorted entries when history exists`() =
        runTest {
            val json =
                """
                [
                    {"id":"1","swimSessionId":"1-1","completedAt":"2023-10-27T10:00:00Z"},
                    {"id":"2","swimSessionId":"1-2","completedAt":"2023-10-27T11:00:00Z"}
                ]
                """.trimIndent()
            every { prefs.getString("history", "") } returns json

            val result = repository.getAll()

            assertThat(result.size).isEqualTo(2)
            // Verify sorting by completedAt descending
            assertThat(result[0].swimSessionId).isEqualTo("1-2")
            assertThat(result[1].swimSessionId).isEqualTo("1-1")
        }

    @Test
    fun `addSession stores session and updates flow`() =
        runTest {
            val session =
                SwimSession(
                    weekPriority = 1,
                    completed = true,
                    week = SwimmingWeek(1),
                    swimSets = emptyList(),
                    completedAt = OffsetDateTime.parse("2023-10-27T10:00:00Z"),
                )
            every { prefs.getString("history", "") } returns ""

            repository.addSession(session)

            verify { editor.putString("history", any()) }
            val observed = repository.observeAll().first()
            assertThat(observed.size).isEqualTo(1)
            assertThat(observed.first().swimSessionId).isEqualTo(session.id)
        }

    @Test
    fun `addSessions stores multiple sessions`() =
        runTest {
            val sessions =
                listOf(
                    SwimSession(1, true, SwimmingWeek(1), emptyList(), OffsetDateTime.parse("2023-10-27T10:00:00Z")),
                    SwimSession(2, true, SwimmingWeek(1), emptyList(), OffsetDateTime.parse("2023-10-27T11:00:00Z")),
                )
            every { prefs.getString("history", "") } returns ""

            repository.addSessions(sessions)

            verify { editor.putString("history", any()) }
            val observed = repository.observeAll().first()
            assertThat(observed.size).isEqualTo(2)
        }

    @Test
    fun `isEmpty returns true when no history`() =
        runTest {
            every { prefs.getString("history", "") } returns ""
            // Re-init to update historyStateFlow
            repository = LocalHistoryRepository(prefs)

            assertThat(repository.isEmpty()).isEqualTo(true)
        }

    @Test
    fun `isEmpty returns false when history exists`() =
        runTest {
            val json = """[{"id":"1","swimSessionId":"1-1","completedAt":"2023-10-27T10:00:00Z"}]"""
            every { prefs.getString("history", "") } returns json
            // Re-init to update historyStateFlow
            repository = LocalHistoryRepository(prefs)

            assertThat(repository.isEmpty()).isEqualTo(false)
        }

    @Test
    fun `clearAll removes history and updates flow`() =
        runTest {
            repository.clearAll()

            verify { editor.putString("history", "") }
            val observed = repository.observeAll().first()
            assertThat(observed).isEmpty()
        }
}
