package com.alexallafi.app.data.local_storage

import android.content.SharedPreferences
import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.alexallafi.app.domain.HistoryEntryNote
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LocalHistoryNotesRepositoryTest {
    private val prefs = mockk<SharedPreferences>(relaxed = true)
    private val editor = mockk<SharedPreferences.Editor>(relaxed = true)
    private lateinit var repository: LocalHistoryNotesRepository

    @BeforeEach
    fun setup() {
        every { prefs.edit() } returns editor
        every { editor.putString(any(), any()) } returns editor
        every { prefs.getString("history_notes", "") } returns ""
        repository = LocalHistoryNotesRepository(prefs)
    }

    @Test
    fun `getAllNotes returns empty map when no notes stored`() =
        runTest {
            assertThat(repository.getAllNotes()).isEmpty()
        }

    @Test
    fun `saveNote stores note and updates flow`() =
        runTest {
            val entryId = "entry_1"
            val note = HistoryEntryNote("Felt good")

            repository.saveNote(entryId, note)

            verify { editor.putString("history_notes", any()) }
            val observed = repository.observeAllNotes().first()
            assertThat(observed[entryId]).isEqualTo(note)
        }

    @Test
    fun `getNote returns correct note`() =
        runTest {
            val json = """{"entry_1":{"notesText":"Felt good"}}"""
            every { prefs.getString("history_notes", "") } returns json
            repository = LocalHistoryNotesRepository(prefs)

            val note = repository.getNote("entry_1")
            assertThat(note?.notesText).isEqualTo("Felt good")
        }

    @Test
    fun `deleteNote removes note and updates flow`() =
        runTest {
            val json = """{"entry_1":{"notesText":"Felt good"}}"""
            every { prefs.getString("history_notes", "") } returns json
            repository = LocalHistoryNotesRepository(prefs)

            repository.deleteNote("entry_1")

            verify { editor.putString("history_notes", any()) }
            val observed = repository.observeAllNotes().first()
            assertThat(observed["entry_1"]).isNull()
        }
}
