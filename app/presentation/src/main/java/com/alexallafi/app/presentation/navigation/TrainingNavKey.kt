package com.alexallafi.app.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface TrainingNavKey {
    @Serializable data object SessionsList : TrainingNavKey
    @Serializable data object NextSession : TrainingNavKey
    @Serializable data object History : TrainingNavKey
    @Serializable data object Settings : TrainingNavKey
}
