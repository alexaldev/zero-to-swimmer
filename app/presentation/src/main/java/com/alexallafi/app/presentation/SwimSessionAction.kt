package com.alexallafi.app.presentation

sealed interface SwimSessionAction {
    data class ExpandSession(val sessionViewItem: SwimSessionListItem.SwimSessionViewItem): SwimSessionAction
    data class CollapseSession(val sessionViewItem: SwimSessionListItem.SwimSessionViewItem): SwimSessionAction
    data class CompletedToggled(val sessionViewItem: SwimSessionListItem): SwimSessionAction
    data object ScrollToNextAvailable : SwimSessionAction
}