package com.alexallafi.app.presentation.nextSession

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexallafi.app.domain.ConfigurationRepository
import com.alexallafi.app.domain.usecase.CompleteSessionUseCase
import com.alexallafi.app.domain.usecase.GetNextAvailableSessionUseCase
import com.alexallafi.app.domain.usecase.SeeFavoriteSessionUseCase
import com.alexallafi.app.presentation.ViewItemsMapper
import com.github.michaelbull.result.mapBoth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NextSessionViewModel(
    private val viewItemsMapper: ViewItemsMapper,
    private val configurationRepository: ConfigurationRepository,
    seeFavoriteSessionUseCase: SeeFavoriteSessionUseCase,
    getNextAvailableSessionUseCase: GetNextAvailableSessionUseCase,
    private val completeSessionUseCase: CompleteSessionUseCase,
) : ViewModel() {
    private val confirmingSessionId = MutableStateFlow<String?>(null)

    val favoriteViewItem: StateFlow<FavoriteSessionViewItem> =
        combine(
            seeFavoriteSessionUseCase.observe(),
            configurationRepository.observePoolSize()
        ) { favoriteResult, poolSize ->
            favoriteResult.mapBoth(
                success = { viewItemsMapper.mapSessionToFavoriteViewItem(it, poolSize) },
                failure = { FavoriteSessionViewItem("-") },
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FavoriteSessionViewItem("-"))

    val nextViewItem: StateFlow<NextSessionViewItem> =
        combine(
            getNextAvailableSessionUseCase.observe(),
            confirmingSessionId,
            configurationRepository.observePoolSize()
        ) { nextResult, confirmingId, poolSize ->
            nextResult.mapBoth(
                success = { nextAvailableSession ->
                    viewItemsMapper.mapSessionToNextViewItem(nextAvailableSession, poolSize, confirmingId == nextAvailableSession.id)
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

