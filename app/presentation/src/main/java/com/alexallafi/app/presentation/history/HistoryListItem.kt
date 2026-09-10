package com.alexallafi.app.presentation.history

sealed class HistoryListItem {
    data class MonthHeader(val month: String) : HistoryListItem()
    data class SessionItem(
        val entryId: String,
        val sessionId: String,
        val sessionTitle: String,
        val completedAt: String
    ) : HistoryListItem()
}
