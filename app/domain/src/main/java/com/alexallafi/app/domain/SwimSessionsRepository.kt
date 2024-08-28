package com.alexallafi.app.domain

import kotlinx.coroutines.flow.Flow

interface SwimSessionsRepository {
    suspend fun getAll(): Result<List<SwimSession>>
    fun observeAll(): Flow<List<SwimSession>>
    suspend fun markAsCompleted(swimSession: SwimSession)
}