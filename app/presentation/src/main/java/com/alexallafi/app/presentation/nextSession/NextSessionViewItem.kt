package com.alexallafi.app.presentation.nextSession

data class NextSessionViewItem(
    val id: String = "",
    val sessionTitle: String = "",
    val sessionSetsText: String = "",
    val totalDistanceText: String = "",
    val showConfirmState: Boolean = false,
)