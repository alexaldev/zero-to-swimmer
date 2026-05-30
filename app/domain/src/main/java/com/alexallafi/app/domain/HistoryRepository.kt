package com.alexallafi.app.domain

import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    suspend fun getAll(): List<HistoryEntry>

    fun observeAll(): Flow<List<HistoryEntry>>

    suspend fun addSession(swimSession: SwimSession)
}
