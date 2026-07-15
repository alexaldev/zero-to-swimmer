package com.alexallafi.app.presentation.trainingProgram

sealed interface SwimSessionListItem {
    data class ProgressOverviewViewItem(
        val totalCompleted: String,
        val nextAvailable: String,
    ) : SwimSessionListItem

    data class SwimSessionViewItem(
        val id: String,
        val title: String = "",
        val message: String = "",
        val isCompleted: Boolean = false,
        val isExpanded: Boolean = false,
        val isFavorite: Boolean = false,
        val swimRounds: String = "",
    ) : SwimSessionListItem

    data class WeekHeaderItem(
        val weekText: String = "",
        val weekProgress: Float = 0f
    ) : SwimSessionListItem
}
