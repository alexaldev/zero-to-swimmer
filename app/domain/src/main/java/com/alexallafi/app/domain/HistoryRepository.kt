package com.alexallafi.app.domain

import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    suspend fun isEmpty(): Boolean

    suspend fun getAll(): List<HistoryEntry>

    fun observeAll(): Flow<List<HistoryEntry>>

    suspend fun addSession(swimSession: SwimSession)

    suspend fun addSessions(sessions: List<SwimSession>)

    suspend fun removeSessionBySessionId(swimSessionId: String)

    suspend fun clearAll()
}
