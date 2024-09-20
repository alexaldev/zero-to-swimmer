package com.alexallafi.app.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.alexallafi.app.domain.SwimSessionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
            .onEach { sessions ->
                _sessionsViewItems.update { viewItemsMapper.mapToViewItems(sessions) }
            }
            .launchIn(viewModelScope)

        viewModelScope.launch { sessionsRepository.getAll() }
    }
}