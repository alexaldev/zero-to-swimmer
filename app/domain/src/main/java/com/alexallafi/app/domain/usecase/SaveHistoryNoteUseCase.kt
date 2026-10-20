package com.alexallafi.app.domain.usecase

import com.alexallafi.app.domain.HistoryEntryId
import com.alexallafi.app.domain.HistoryEntryNote
import com.alexallafi.app.domain.HistoryNotesRepository
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class SaveHistoryNoteUseCase(
    private val historyNotesRepository: HistoryNotesRepository,
) : BaseCoroutineUseCase<Unit, Unit> {
    override suspend fun invoke(vararg params: Any): Result<Unit, Unit> {
        val id = params.getOrNull(0) as? HistoryEntryId ?: return Ok(Unit)
        val noteText = params.getOrNull(1) as? String ?: ""

        historyNotesRepository.saveNote(id, HistoryEntryNote(noteText))
        return Ok(Unit)
    }

    override fun observe(): Flow<Result<Unit, Unit>> = emptyFlow()
}
