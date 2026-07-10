package com.alexallafi.app.domain

import kotlinx.coroutines.flow.Flow

interface ConfigurationRepository {
    suspend fun setPoolSize(size: PoolSize)

    suspend fun getPoolSize(): PoolSize

    suspend fun toggleFavoriteSession(id: String)

    suspend fun getFavoriteSessionId(): String?

    fun observeFavoriteSession(): Flow<String?>

    fun observePoolSize(): Flow<PoolSize>
}
