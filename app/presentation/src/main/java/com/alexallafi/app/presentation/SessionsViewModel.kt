package com.alexallafi.app.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.alexallafi.app.domain.ConfigurationRepository
import com.alexallafi.app.domain.HistoryRepository
import com.alexallafi.app.domain.SwimSessionsRepository
import com.alexallafi.app.domain.usecase.ViewProgramUseCase
import com.alexallafi.app.presentation.SwimSessionListItem.SwimSessionViewItem
import com.github.michaelbull.result.mapBoth
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class SessionsViewModel(
    private val sessionsRepository: SwimSessionsRepository,
    private val historyRepository: HistoryRepository,
    private val viewItemsMapper: ViewItemsMapper,
    private val configurationRepository: ConfigurationRepository,
    private val memoryStateHandle: SavedStateHandle,
    private val viewProgramUseCase: ViewProgramUseCase,
) : ViewModel() {
    private val expandedSessionIds =
        memoryStateHandle.getStateFlow<Set<String>>(EXPANDED_IDS_KEY, emptySet())

    private val _sessionsViewItems =
        combine(
            viewProgramUseCase.observe(),
            configurationRepository.observeFavoriteSession(),
            configurationRepository.observePoolSize(),
            expandedSessionIds,
        ) { sessions, favoriteId, poolSize, expandedIds ->
            sessions.mapBoth(
                success = { viewItemsMapper.mapToViewItems(it, poolSize, favoriteId, expandedIds) },
                failure = { emptyList() },
            )
        }.stateIn(viewModelScope, WhileSubscribed(5.seconds.inWholeMilliseconds), emptyList())

    val sessionsViewItems = _sessionsViewItems.asLiveData()

    fun nextAvailableSessionPosition(): Int =
        this._sessionsViewItems.value
            .indexOfFirst { swimSession -> swimSession is SwimSessionViewItem && swimSession.isCompleted.not() }

    fun onAction(action: SwimSessionAction) {
        when (action) {
            is SwimSessionAction.CollapseSession -> {
                memoryStateHandle[EXPANDED_IDS_KEY] = expandedSessionIds.value - action.sessionViewItem.id
            }

            is SwimSessionAction.ExpandSession -> {
                memoryStateHandle[EXPANDED_IDS_KEY] = expandedSessionIds.value + action.sessionViewItem.id
            }

            is SwimSessionAction.CompletedToggled -> {
                val selectedSession = action.sessionViewItem as? SwimSessionViewItem ?: return
                viewModelScope.launch {
                    sessionsRepository.toggleCompleted(selectedSession.id)
                    sessionsRepository.getById(selectedSession.id)?.let { updated ->
                        if (updated.completed) {
                            historyRepository.addSession(updated)
                        } else {
                            historyRepository.removeSessionBySessionId(updated.id)
                        }
                    }
                }
            }

            SwimSessionAction.ScrollToNextAvailable -> {
                // This is handled by Fragment for now but could be an event
            }

            is SwimSessionAction.FavoriteToggled -> {
                val selectedSession = action.sessionViewItem
                viewModelScope.launch {
                    configurationRepository.toggleFavoriteSession(selectedSession.id)
                }
            }
        }
    }
}

sealed interface ScreenState {
    data class SessionsList(
        val items: List<SwimSessionListItem>,
    ) : ScreenState

    data class ScrollToPosition(
        val position: Int,
    ) : ScreenState
}

private const val EXPANDED_IDS_KEY = "expanded_session_ids"
