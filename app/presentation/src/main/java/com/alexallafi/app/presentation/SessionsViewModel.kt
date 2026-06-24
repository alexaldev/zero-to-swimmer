package com.alexallafi.app.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.alexallafi.app.domain.ConfigurationRepository
import com.alexallafi.app.domain.HistoryRepository
import com.alexallafi.app.domain.SwimSessionsRepository
import com.alexallafi.app.presentation.SwimSessionListItem.SwimSessionViewItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SessionsViewModel(
    private val sessionsRepository: SwimSessionsRepository,
    private val historyRepository: HistoryRepository,
    private val viewItemsMapper: ViewItemsMapper,
    private val configurationRepository: ConfigurationRepository,
    private val memoryStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _sessionsViewItems: MutableStateFlow<List<SwimSessionListItem>> =
        MutableStateFlow(emptyList())
    val sessionsViewItems = _sessionsViewItems.asLiveData()

    private val expandedSessionIds =
        memoryStateHandle.getStateFlow<Set<String>>(EXPANDED_IDS_KEY, emptySet())

    init {
        combine(
            sessionsRepository.observeAll(),
            configurationRepository.observeFavoriteSession(),
            expandedSessionIds,
        ) { sessions, _, expandedIds ->
            viewItemsMapper.mapToViewItems(sessions, expandedIds)
        }.onEach {
            _sessionsViewItems.value = it
        }.launchIn(viewModelScope)
    }

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
