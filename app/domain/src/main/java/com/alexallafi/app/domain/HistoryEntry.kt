package com.alexallafi.app.domain

import java.time.OffsetDateTime

data class HistoryEntry(
    private val id: String,
    val swimSessionId: String,
    val completedAt: OffsetDateTime,
)
