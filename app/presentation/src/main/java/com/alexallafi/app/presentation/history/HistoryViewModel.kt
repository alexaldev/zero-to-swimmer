package com.alexallafi.app.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexallafi.app.domain.SwimSessionsRepository
import com.alexallafi.app.domain.usecase.SaveHistoryNoteUseCase
import com.alexallafi.app.domain.usecase.ViewHistoryUseCase
import com.alexallafi.app.presentation.ViewItemsMapper
import com.github.michaelbull.result.mapBoth
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.Locale

class HistoryViewModel(
    private val swimSessionsRepository: SwimSessionsRepository,
    viewHistoryUseCase: ViewHistoryUseCase,
    private val saveHistoryNoteUseCase: SaveHistoryNoteUseCase,
    private val viewItemsMapper: ViewItemsMapper,
) : ViewModel() {
    private val monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())

    val historyItems: StateFlow<List<HistoryListItem>> =
        viewHistoryUseCase
            .observe()
            .map { historyEntriesResult ->
                historyEntriesResult.mapBoth(
                    success = { historyEntriesWithNotes ->
                        val listItems = mutableListOf<HistoryListItem>()

                        historyEntriesWithNotes
                            .groupBy { it.entry.completedAt.format(monthFormatter) }
                            .forEach { (month, entries) ->
                                listItems.add(HistoryListItem.MonthHeader(month))
                                entries.forEach { entryWithNote ->
                                    val entry = entryWithNote.entry
                                    val session = swimSessionsRepository.getById(entry.swimSessionId)
                                    listItems.add(
                                        HistoryListItem.SessionItem(
                                            entryId = entry.id,
                                            sessionId = entry.swimSessionId,
                                            sessionTitle = if (session != null) viewItemsMapper.weekAndDayTitleFor(session) else "",
                                            completedAt = viewItemsMapper.completedAtMessage(entry.completedAt),
                                            notes = entryWithNote.note?.notesText ?: "",
                                        ),
                                    )
                                }
                            }
                        listItems
                    },
                    failure = { emptyList() },
                )
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onNoteSave(
        entryId: String,
        noteText: String,
    ) {
        viewModelScope.launch {
            saveHistoryNoteUseCase.invoke(entryId, noteText)
        }
    }
}
