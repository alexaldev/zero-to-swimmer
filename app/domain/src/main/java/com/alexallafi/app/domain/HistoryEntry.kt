package com.alexallafi.app.domain

import java.time.OffsetDateTime

typealias HistoryEntryId = String

data class HistoryEntry(
    val id: HistoryEntryId,
    val swimSessionId: String,
    val completedAt: OffsetDateTime,
)
