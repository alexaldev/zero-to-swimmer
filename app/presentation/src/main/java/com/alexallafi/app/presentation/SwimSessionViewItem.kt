package com.alexallafi.app.presentation

sealed interface SwimSessionListItem {

    data class SwimSessionViewItem(
        val title: String = "",
        val message: String = "",
        val isCompleted: Boolean = false,
        val isExpanded: Boolean = false,
        val swimRounds: String = ""
    ): SwimSessionListItem

    data class HeaderItem(
        val startText: String = "",
        val endText: String = ""
    ): SwimSessionListItem
}
