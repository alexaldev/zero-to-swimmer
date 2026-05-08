package com.alexallafi.app.domain

interface ConfigurationRepository {
    suspend fun setPoolSize(size: PoolSize)

    suspend fun getPoolSize(): PoolSize
}
