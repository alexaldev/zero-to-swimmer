package com.alexallafi.app.data.local_storage

import kotlinx.serialization.Serializable

@Serializable
data class StorableHistoryEntryNote(
    val notesText: String,
)
