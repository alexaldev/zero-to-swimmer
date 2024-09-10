package com.alexallafi.app.domain

import kotlinx.coroutines.flow.Flow

interface SwimSessionsRepository {
    suspend fun getAll(): Result<List<SwimSession>>
    fun observeAll(): Flow<List<SwimSession>>
    suspend fun markAsCompleted(swimSession: SwimSession)
    suspend fun addAll(swimSessions: List<SwimSession>)
    suspend fun totalMetersForWeek(swimmingWeek: SwimmingWeek): Int
    suspend fun completedMetersForWeek(swimmingWeek: SwimmingWeek): Int
    suspend fun isWeekCompleted(swimmingWeek: SwimmingWeek): Boolean
    suspend fun numberOfWeeks(): Int
}