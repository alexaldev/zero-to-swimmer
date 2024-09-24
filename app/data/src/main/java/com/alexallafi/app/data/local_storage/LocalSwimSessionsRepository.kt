package com.alexallafi.app.data.local_storage

import android.content.Context
import com.alexallafi.app.data.toDataModel
import com.alexallafi.app.data.toDomainModel
import com.alexallafi.app.domain.SwimSession
import com.alexallafi.app.domain.SwimSessionsRepository
import com.alexallafi.app.domain.SwimmingWeek
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class LocalSwimSessionsRepository(
    private val context: Context,
    private val ioDispatcher: CoroutineDispatcher
) : SwimSessionsRepository {

    private val _sessionsFlow = MutableStateFlow<List<SwimSession>>(emptyList())

    override suspend fun getAll(): Result<List<SwimSession>> {

        return withContext(ioDispatcher) {

            val file = File(context.filesDir, "swimming_sessions.json")

            if (file.exists()) {
                val encoded = file.readText()
                ensureActive()
                try {
                    val decoded = Json.decodeFromString<List<com.alexallafi.app.data.local_storage.SwimSession>>(encoded)
                    val decodedDomain = decoded.toDomainModel()
                    _sessionsFlow.update { decodedDomain }
                    Result.success(decodedDomain)
                } catch (e: SerializationException) {
                    Result.failure(e)
                }
            } else {
                Result.failure(IllegalStateException("Swimming sessions file does not exist"))
            }
        }
    }

    override fun observeAll(): Flow<List<SwimSession>> = _sessionsFlow

    override suspend fun markAsCompleted(swimSession: SwimSession) {

        withContext(ioDispatcher) {

            val allSessions = getAll().getOrElse { return@withContext }.toMutableList()

            val sessionIndex = allSessions.indexOfFirst { it.weekPriority == swimSession.weekPriority }
            if (sessionIndex != -1) {
                val updatedSession = allSessions[sessionIndex].copy(completed = true)
                allSessions[sessionIndex] = updatedSession
                _sessionsFlow.emit(allSessions)
                addAll(allSessions)
            }
        }
    }

    override suspend fun addAll(swimSessions: List<SwimSession>) {

        withContext(ioDispatcher) {

            val file = File(context.filesDir, "swimming_sessions.json")

            val sessionsEncoded = Json.encodeToString(swimSessions.toDataModel())

            file.writeText(sessionsEncoded).also {
                _sessionsFlow.update { swimSessions }
            }
        }
    }

    override suspend fun totalMetersForWeek(swimmingWeek: SwimmingWeek): Int {
        return withContext(ioDispatcher) {

            getAll()
                .getOrElse { return@withContext 0 }
                .filter { it.week == swimmingWeek }
                .sumOf { it.swimSets.sumOf { set -> set.meters * set.count } }
        }
    }

    override suspend fun completedMetersForWeek(swimmingWeek: SwimmingWeek): Int {

        return withContext(ioDispatcher) {
            getAll()
                .getOrElse { return@withContext 0 }
                .filter { it.week == swimmingWeek && it.completed }
                .sumOf { it.swimSets.sumOf { set -> set.meters * set.count }  }
        }
    }

    override suspend fun isWeekCompleted(swimmingWeek: SwimmingWeek): Boolean {
        return withContext(ioDispatcher) {
            getAll()
                .getOrThrow()
                .filter { it.week == swimmingWeek }
                .all { it.completed }
        }
    }

    override suspend fun numberOfWeeks(): Int {
        return withContext(ioDispatcher) {
            getAll()
                .getOrElse { return@withContext 0 }
                .maxOf { it.week.value }
        }
    }
}