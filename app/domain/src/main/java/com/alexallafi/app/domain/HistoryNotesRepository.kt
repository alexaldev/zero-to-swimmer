package com.alexallafi.app.domain

import kotlinx.coroutines.flow.Flow

interface HistoryNotesRepository {
    fun observeAllNotes(): Flow<Map<HistoryEntryId, HistoryEntryNote>>

    suspend fun getAllNotes(): Map<HistoryEntryId, HistoryEntryNote>

    suspend fun getNote(id: HistoryEntryId): HistoryEntryNote?

    suspend fun saveNote(
        id: HistoryEntryId,
        note: HistoryEntryNote,
    )

    suspend fun deleteNote(id: HistoryEntryId)
}
