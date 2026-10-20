package com.alexallafi.app.domain.usecase

import com.alexallafi.app.domain.HistoryEntryWithNote
import com.alexallafi.app.domain.HistoryNotesRepository
import com.alexallafi.app.domain.HistoryRepository
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ViewHistoryUseCase(
    private val historyRepository: HistoryRepository,
    private val historyNotesRepository: HistoryNotesRepository,
) : BaseCoroutineUseCase<List<HistoryEntryWithNote>, Unit> {
    override suspend fun invoke(vararg params: Any): Result<List<HistoryEntryWithNote>, Unit> {
        val entries = historyRepository.getAll()
        val notes = historyNotesRepository.getAllNotes()
        return Ok(entries.map { HistoryEntryWithNote(it, notes[it.id]) })
    }

    override fun observe(): Flow<Result<List<HistoryEntryWithNote>, Unit>> =
        combine(
            historyRepository.observeAll(),
            historyNotesRepository.observeAllNotes(),
        ) { entries, notes ->
            Ok(entries.map { HistoryEntryWithNote(it, notes[it.id]) })
        }
}
