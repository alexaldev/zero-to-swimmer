package com.alexallafi.app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.alexallafi.app.domain.InitialDataPopulator
import com.alexallafi.app.domain.SwimSessionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SessionsViewModel(
    private val sessionsRepository: SwimSessionsRepository,
    private val viewItemsMapper: ViewItemsMapper,
    private val initialDataPopulator: InitialDataPopulator
) : ViewModel() {

    private val _sessionsViewItems: MutableStateFlow<List<SwimSessionListItem>> = MutableStateFlow(
        emptyList()
    )
    val sessionViewItems = _sessionsViewItems.asLiveData()

    init {

        sessionsRepository
            .observeAll()
            .onEach { sessions ->
                _sessionsViewItems.update { viewItemsMapper.mapToViewItems(sessions) }
            }
            .launchIn(viewModelScope)

        viewModelScope.launch { populateDataIfNeeded() }
    }

    private suspend fun populateDataIfNeeded() {
        if (sessionsRepository.getAll().isFailure) {
            sessionsRepository.addAll(initialDataPopulator.createSessions())
        }
    }

    fun onAction(action: SwimSessionAction) {
        when(action) {
            is SwimSessionAction.CollapseSession -> {

                val mutableCurrentData = _sessionsViewItems.value.toMutableList()

                val indexToUpdate = mutableCurrentData
                    .indexOf(action.sessionViewItem.copy(isExpanded = false))


            }
            is SwimSessionAction.ExpandSession -> TODO()
            is SwimSessionAction.SetAsCompleted -> TODO()
        }
    }
}