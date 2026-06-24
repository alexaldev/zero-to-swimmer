package com.alexallafi.app.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexallafi.app.domain.HistoryRepository
import com.alexallafi.app.domain.SwimSessionsRepository
import com.alexallafi.app.presentation.ViewItemsMapper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.format.DateTimeFormatter
import java.util.Locale

class HistoryViewModel(
    private val swimSessionsRepository: SwimSessionsRepository,
    private val historyRepository: HistoryRepository,
    private val viewItemsMapper: ViewItemsMapper,
) : ViewModel() {
    private val monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())

    val historyItems: StateFlow<List<HistoryListItem>> =
        combine(
            historyRepository.observeAll(),
            swimSessionsRepository.observeAll()
        ) { historyEntries, _ ->
            val listItems = mutableListOf<HistoryListItem>()

            historyEntries
                .groupBy { it.completedAt.format(monthFormatter) }
                .forEach { (month, entries) ->
                    listItems.add(HistoryListItem.MonthHeader(month))
                    entries.forEach { entry ->
                        val session = swimSessionsRepository.getById(entry.swimSessionId)
                        listItems.add(
                            HistoryListItem.SessionItem(
                                entryId = entry.id,
                                sessionId = entry.swimSessionId,
                                sessionTitle = if (session != null) viewItemsMapper.weekAndDayTitleFor(session) else "",
                                completedAt = viewItemsMapper.completedAtMessage(entry.completedAt),
                            ),
                        )
                    }
                }
            listItems
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
