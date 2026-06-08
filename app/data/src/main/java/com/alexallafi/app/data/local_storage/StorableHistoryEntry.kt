package com.alexallafi.app.data.local_storage

import kotlinx.serialization.Serializable

@Serializable
data class StorableHistoryEntry(
    val id: String,
    val swimSessionId: String,
    val completedAt: String,
)
