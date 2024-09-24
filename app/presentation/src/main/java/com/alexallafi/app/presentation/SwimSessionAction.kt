package com.alexallafi.app.presentation

sealed interface SwimSessionAction {
    data class ExpandSession(val sessionViewItem: SwimSessionListItem): SwimSessionAction
    data class CollapseSession(val sessionViewItem: SwimSessionListItem.SwimSessionViewItem): SwimSessionAction
    data class SetAsCompleted(val sessionViewItem: SwimSessionListItem): SwimSessionAction
}