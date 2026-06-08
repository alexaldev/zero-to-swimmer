package com.alexallafi.app.presentation.history

sealed class HistoryListItem {
    data class MonthHeader(val month: String) : HistoryListItem()
    data class SessionItem(
        val id: String,
        val sessionTitle: String,
        val completedAt: String
    ) : HistoryListItem()
}
