package com.alexallafi.app.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.alexallafi.app.domain.SwimSessionsRepository
import com.alexallafi.app.presentation.ViewItemsMapper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HistoryViewModel(
    swimSessionsRepository: SwimSessionsRepository,
    private val viewItemsMapper: ViewItemsMapper,
) : ViewModel() {
    val historyItems =
        swimSessionsRepository
            .observeAll()
            .map { sessions ->
                sessions
                    .filter { it.completed }
                    .sortedByDescending { it.completedAt }
                    .map { session -> viewItemsMapper.toSwimSessionViewItem(session) }
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
            .asLiveData()
}
