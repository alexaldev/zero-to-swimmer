package com.alexallafi.app.data.local_storage

import android.content.Context
import androidx.core.content.edit
import com.alexallafi.app.domain.ConfigurationRepository
import com.alexallafi.app.domain.PoolSize
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class PrefsConfigurationRepository(
    context: Context,
    private val ioDispatcher: CoroutineDispatcher,
) : ConfigurationRepository {
    private val sessionPrefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)

    override suspend fun setPoolSize(size: PoolSize) {
        withContext(ioDispatcher) {
            sessionPrefs.edit { putInt("poolSize", size.toIntValue()) }
        }
    }

    override suspend fun getPoolSize(): PoolSize =
        withContext(ioDispatcher) {
            PoolSize.fromInt(sessionPrefs.getInt("poolSize", 50))
        }

    override suspend fun setFavoriteSession(id: String) {
        withContext(ioDispatcher) {
            sessionPrefs.edit { putString("favoriteSession", id) }
        }
    }

    override suspend fun getFavoriteSessionId(): String? =
        withContext(ioDispatcher) {
            sessionPrefs.getString("favoriteSession", null)
        }
}
