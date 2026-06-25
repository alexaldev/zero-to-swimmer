package com.alexallafi.app.presentation.nextSession

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexallafi.app.domain.usecase.CompleteSessionUseCase
import com.alexallafi.app.domain.usecase.GetNextAvailableSessionUseCase
import com.alexallafi.app.domain.usecase.SeeFavoriteSessionUseCase
import com.alexallafi.app.presentation.ViewItemsMapper
import com.github.michaelbull.result.mapBoth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NextSessionViewModel(
    private val viewItemsMapper: ViewItemsMapper,
    seeFavoriteSessionUseCase: SeeFavoriteSessionUseCase,
    getNextAvailableSessionUseCase: GetNextAvailableSessionUseCase,
    private val completeSessionUseCase: CompleteSessionUseCase,
) : ViewModel() {
    private val confirmingSessionId = MutableStateFlow<String?>(null)

    val favoriteViewItem: StateFlow<FavoriteSessionViewItem> =
        seeFavoriteSessionUseCase
            .observe()
            .map { favoriteResult ->
                favoriteResult.mapBoth(
                    success = { viewItemsMapper.mapSessionToFavoriteViewItem(it) },
                    { FavoriteSessionViewItem("-") },
                )
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FavoriteSessionViewItem("-"))

    val nextViewItem: StateFlow<NextSessionViewItem> =
        combine(
            getNextAvailableSessionUseCase.observe(),
            confirmingSessionId,
        ) { nextResult, confirmingId ->
            nextResult.mapBoth(
                success = { nextAvailableSession ->
                    viewItemsMapper.mapSessionToNextViewItem(nextAvailableSession, confirmingId == nextAvailableSession.id)
                },
                failure = { NextSessionViewItem() },
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NextSessionViewItem())

    fun onAction(action: UserAction) {
        when (action) {
            is UserAction.MarkSessionAsCompleted -> {
                confirmingSessionId.value = action.sessionId
            }

            UserAction.ConfirmCompletion -> {
                val sessionId = confirmingSessionId.value ?: return
                viewModelScope.launch {
                    completeSessionUseCase.invoke(sessionId)
                    confirmingSessionId.value = null
                }
            }

            UserAction.CancelCompletion -> {
                confirmingSessionId.value = null
            }
        }
    }
}

sealed interface UserAction {
    data class MarkSessionAsCompleted(
        val sessionId: String,
    ) : UserAction

    data object ConfirmCompletion : UserAction

    data object CancelCompletion : UserAction
}

data class FavoriteSessionViewItem(
    val sessionSetsText: String = "",
)

data class NextSessionViewItem(
    val id: String = "",
    val sessionTitle: String = "",
    val sessionSetsText: String = "",
    val totalDistanceText: String = "",
    val showConfirmState: Boolean = false,
)
