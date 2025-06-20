package com.alexallafi.app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.alexallafi.app.domain.InitialDataPopulator
import com.alexallafi.app.domain.SwimSessionsRepository
import com.alexallafi.app.presentation.SwimSessionListItem.SwimSessionViewItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SessionsViewModel(
    private val sessionsRepository: SwimSessionsRepository,
    private val viewItemsMapper: ViewItemsMapper
) : ViewModel() {

    private val _sessionsViewItems: MutableStateFlow<List<SwimSessionListItem>> = MutableStateFlow(
        emptyList()
    )
    val sessionViewItems = _sessionsViewItems.asLiveData()

    init {

        sessionsRepository
            .observeAll()
            .map { sessions ->
                _sessionsViewItems.update { viewItemsMapper.mapToViewItems(sessions) }
            }
            .launchIn(viewModelScope)

    }

    fun nextAvailableSessionPosition(): Int {
        return this._sessionsViewItems.value
            .indexOfFirst { swimSession -> swimSession is SwimSessionViewItem && swimSession.isCompleted.not()  }
    }

    fun onAction(action: SwimSessionAction) {
        when(action) {
            is SwimSessionAction.CollapseSession -> {

                _sessionsViewItems.update { currentList ->
                    currentList.map { item ->
                        if (item is SwimSessionViewItem && item == action.sessionViewItem) {
                            item.copy(isExpanded = false)
                        } else
                            item
                    }
                }
            }
            is SwimSessionAction.ExpandSession -> {
                _sessionsViewItems.update { currentList ->
                    currentList.map { item ->
                        if (item is SwimSessionViewItem && item == action.sessionViewItem) {
                            item.copy(isExpanded = true)
                        } else
                            item
                    }
                }
            }
            is SwimSessionAction.CompletedToggled -> {

                val selectedSession = action.sessionViewItem as? SwimSessionViewItem ?: return

                viewModelScope.launch { sessionsRepository.toggleCompleted(selectedSession.id) }
            }

            SwimSessionAction.ScrollToNextAvailable -> TODO()
        }
    }
}

sealed interface ScreenState {
    data class SessionsList(val items: List<SwimSessionListItem>): ScreenState
    data class ScrollToPosition(val position: Int): ScreenState
}