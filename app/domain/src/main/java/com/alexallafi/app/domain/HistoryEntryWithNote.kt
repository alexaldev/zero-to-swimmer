package com.alexallafi.app.domain

data class HistoryEntryWithNote(
    val entry: HistoryEntry,
    val note: HistoryEntryNote?,
)
