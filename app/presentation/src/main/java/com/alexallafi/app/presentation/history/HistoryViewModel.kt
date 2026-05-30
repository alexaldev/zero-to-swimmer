package com.alexallafi.app.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.alexallafi.app.domain.HistoryRepository
import com.alexallafi.app.domain.SwimSession
import com.alexallafi.app.domain.SwimSessionsRepository
import com.alexallafi.app.presentation.SwimSessionListItem
import com.alexallafi.app.presentation.ViewItemsMapper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HistoryViewModel(
    swimSessionsRepository: SwimSessionsRepository,
    private val historyRepository: HistoryRepository,
    private val viewItemsMapper: ViewItemsMapper,
) : ViewModel() {
    val historyItems =
        historyRepository
            .observeAll()
            .map { historyItems ->
                historyItems.map { historyEntry ->
                    val session = swimSessionsRepository.getById(historyEntry.swimSessionId)
                    HistoryViewItem(
                        sessionTitle = if (session != null) viewItemsMapper.weekAndDayTitleFor(session) else "",
                        completedAt = viewItemsMapper.completedAtMessage(historyEntry.completedAt),
                    )
                }
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

//    val historyItems: StateFlow<List<HistoryViewItem>> =
//        swimSessionsRepository
//            .observeAll()
//            .map { sessions ->
//                sessions
//                    .filter { it.completed }
//                    .sortedByDescending { it.completedAt }
//                    .map { session ->
//                        HistoryViewItem(
//                            sessionTitle = viewItemsMapper.weekAndDayTitleFor(session),
//                            completedAt = viewItemsMapper.completedAtMessage(session),
//                        )
//                    }
//            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

data class HistoryViewItem(
    val sessionTitle: String = "",
    val completedAt: String = "",
)
