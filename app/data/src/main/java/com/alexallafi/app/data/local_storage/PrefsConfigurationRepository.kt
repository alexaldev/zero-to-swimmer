package com.alexallafi.app.data.local_storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.alexallafi.app.domain.ConfigurationRepository
import com.alexallafi.app.domain.PoolSize
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

class PrefsConfigurationRepository(
    context: Context,
    private val ioDispatcher: CoroutineDispatcher,
) : ConfigurationRepository {
    private val sessionPrefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)

    override fun observeFavoriteSession(): Flow<String?> =
        callbackFlow {
            val listener =
                SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
                    if (key == "favoriteSession") {
                        trySend(prefs.getString("favoriteSession", null))
                    }
                }
            sessionPrefs.registerOnSharedPreferenceChangeListener(listener)
            awaitClose { sessionPrefs.unregisterOnSharedPreferenceChangeListener(listener) }
        }.onStart { emit(sessionPrefs.getString("favoriteSession", null)) }
            .buffer(Channel.UNLIMITED)

    override suspend fun setPoolSize(size: PoolSize) {
        withContext(ioDispatcher) {
            sessionPrefs.edit { putInt("poolSize", size.toIntValue()) }
        }
    }

    override suspend fun getPoolSize(): PoolSize =
        withContext(ioDispatcher) {
            PoolSize.fromInt(sessionPrefs.getInt("poolSize", 50))
        }

    override suspend fun toggleFavoriteSession(id: String) {
        withContext(ioDispatcher) {
            if (sessionPrefs.getString("favoriteSession", null) == id) {
                sessionPrefs.edit { remove("favoriteSession") }
                return@withContext
            }
            sessionPrefs.edit { putString("favoriteSession", id) }
        }
    }

    override suspend fun getFavoriteSessionId(): String? =
        withContext(ioDispatcher) {
            sessionPrefs.getString("favoriteSession", null)
        }
}
