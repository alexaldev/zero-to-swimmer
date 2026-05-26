package com.alexallafi.app.presentation.nextSession

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexallafi.app.domain.ConfigurationRepository
import com.alexallafi.app.domain.SwimSessionsRepository
import com.alexallafi.app.presentation.SwimSessionListItem
import com.alexallafi.app.presentation.ViewItemsMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NextSessionViewModel(
    private val swimSessionsRepository: SwimSessionsRepository,
    private val configRepository: ConfigurationRepository,
    private val viewItemsMapper: ViewItemsMapper,
) : ViewModel() {
    private val confirmingSessionId = MutableStateFlow<String?>(null)

    val favoriteViewItem: StateFlow<FavoriteSessionViewItem> =
        combine(
            swimSessionsRepository.observeAll(),
            configRepository.observeFavoriteSession(),
        ) { sessions, favoriteId ->
            val favoriteSession = sessions.find { it.id == favoriteId }
            val favoriteText =
                favoriteSession?.let {
                    val sessionIdText = viewItemsMapper.titleFor(it)
                    val setsText = viewItemsMapper.mapSwimRoundsFor(it.swimSets, configRepository.getPoolSize())
                    "$sessionIdText\n$setsText"
                } ?: "-"
            FavoriteSessionViewItem(favoriteText)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FavoriteSessionViewItem("-"))

    val nextViewItem: StateFlow<NextSessionViewItem> =
        combine(
            swimSessionsRepository.observeAll(),
            confirmingSessionId,
        ) { sessions, confirmingId ->
            val nextAvailableSession = sessions.firstOrNull { !it.completed }
            nextAvailableSession ?: return@combine NextSessionViewItem()

            val nextAvailableText =
                viewItemsMapper.mapSwimRoundsFor(
                    nextAvailableSession.swimSets,
                    configRepository.getPoolSize(),
                )

            val isConfirming = confirmingId == nextAvailableSession.id

            NextSessionViewItem(
                id = nextAvailableSession.id,
                sessionSetsText = nextAvailableText,
                showConfirmState = isConfirming,
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NextSessionViewItem())

    val overviewViewItem: StateFlow<SwimSessionListItem.ProgressOverviewViewItem?> =
        swimSessionsRepository
            .observeAll()
            .map {
                viewItemsMapper.getOverview(it) as? SwimSessionListItem.ProgressOverviewViewItem
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun onAction(action: UserAction) {
        when (action) {
            is UserAction.MarkSessionAsCompleted -> {
                confirmingSessionId.value = action.sessionId
            }

            UserAction.ConfirmCompletion -> {
                val sessionId = confirmingSessionId.value ?: return
                viewModelScope.launch {
                    swimSessionsRepository.toggleCompleted(sessionId)
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
    val sessionSetsText: String = "",
    val showConfirmState: Boolean = false,
)
