package com.alexallafi.app.data.local_storage

import android.content.SharedPreferences
import androidx.core.content.edit
import com.alexallafi.app.data.toDomainHistoryEntries
import com.alexallafi.app.data.toStorableHistoryEntry
import com.alexallafi.app.domain.HistoryEntry
import com.alexallafi.app.domain.HistoryRepository
import com.alexallafi.app.domain.SwimSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json

class LocalHistoryRepository(
    private val prefs: SharedPreferences,
) : HistoryRepository {
    private val historyStateFlow = MutableStateFlow<List<HistoryEntry>>(getAllSync())

    override suspend fun getAll(): List<HistoryEntry> {
        val encoded = prefs.getString("history", "") ?: ""
        if (encoded.isEmpty()) return emptyList()

        val decoded =
            Json.decodeFromString<List<StorableHistoryEntry>>(
                encoded,
            )
        return decoded.toDomainHistoryEntries().sortedByDescending { it.completedAt }
    }

    private fun getAllSync(): List<HistoryEntry> {
        val encoded = prefs.getString("history", "") ?: ""
        if (encoded.isEmpty()) return emptyList()
        val decoded = Json.decodeFromString<List<StorableHistoryEntry>>(encoded)
        return decoded.toDomainHistoryEntries().sortedByDescending { it.completedAt }
    }

    override fun observeAll(): Flow<List<HistoryEntry>> = historyStateFlow.asStateFlow()

    override suspend fun addSession(swimSession: SwimSession) {
        val toAdd = swimSession.toStorableHistoryEntry()
        val currentHistory = getAllAsDataModel().toMutableList()

        prefs.edit {
            val updated = currentHistory + toAdd
            val toStore = Json.encodeToString(updated)
            historyStateFlow.update { updated.toDomainHistoryEntries() }
            putString("history", toStore)
        }
    }

    private fun getAllAsDataModel(): List<StorableHistoryEntry> {
        val encoded = prefs.getString("history", "") ?: ""
        if (encoded.isEmpty()) return emptyList()

        val decoded =
            Json.decodeFromString<List<StorableHistoryEntry>>(
                encoded,
            )
        return decoded
    }

    override suspend fun addSessions(sessions: List<SwimSession>) {
        val current = getAllAsDataModel()
        val updated = sessions.map { it.toStorableHistoryEntry() } + current
        val encoded = Json.encodeToString(updated)
        prefs.edit {
            putString("history", encoded)
        }
        historyStateFlow.update { updated.toDomainHistoryEntries() }
    }

    override suspend fun isEmpty(): Boolean = historyStateFlow.value.isEmpty()
}
