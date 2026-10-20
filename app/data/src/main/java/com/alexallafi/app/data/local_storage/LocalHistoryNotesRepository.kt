package com.alexallafi.app.data.local_storage

import android.content.SharedPreferences
import androidx.core.content.edit
import com.alexallafi.app.domain.HistoryEntryId
import com.alexallafi.app.domain.HistoryEntryNote
import com.alexallafi.app.domain.HistoryNotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LocalHistoryNotesRepository(
    private val prefs: SharedPreferences,
) : HistoryNotesRepository {
    private val notesStateFlow = MutableStateFlow<Map<HistoryEntryId, HistoryEntryNote>>(getAllSync())

    private fun getAllSync(): Map<HistoryEntryId, HistoryEntryNote> {
        val encoded = prefs.getString("history_notes", "") ?: ""
        if (encoded.isEmpty()) return emptyMap()
        return try {
            val decoded = Json.decodeFromString<Map<String, StorableHistoryEntryNote>>(encoded)
            decoded.mapValues { HistoryEntryNote(it.value.notesText) }
        } catch (e: Exception) {
            emptyMap()
        }
    }

    override fun observeAllNotes(): Flow<Map<HistoryEntryId, HistoryEntryNote>> = notesStateFlow.asStateFlow()

    override suspend fun getAllNotes(): Map<HistoryEntryId, HistoryEntryNote> = notesStateFlow.value

    override suspend fun getNote(id: HistoryEntryId): HistoryEntryNote? = notesStateFlow.value[id]

    override suspend fun saveNote(
        id: HistoryEntryId,
        note: HistoryEntryNote,
    ) {
        val currentNotes = getAllSync().toMutableMap()
        currentNotes[id] = note

        saveToPrefs(currentNotes)
        notesStateFlow.update { currentNotes }
    }

    override suspend fun deleteNote(id: HistoryEntryId) {
        val currentNotes = getAllSync().toMutableMap()
        currentNotes.remove(id)

        saveToPrefs(currentNotes)
        notesStateFlow.update { currentNotes }
    }

    private fun saveToPrefs(notes: Map<HistoryEntryId, HistoryEntryNote>) {
        val toStore = notes.mapValues { StorableHistoryEntryNote(it.value.notesText) }
        val encoded = Json.encodeToString(toStore)
        prefs.edit {
            putString("history_notes", encoded)
        }
    }
}
