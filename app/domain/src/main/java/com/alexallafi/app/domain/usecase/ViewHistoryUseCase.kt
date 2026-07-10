package com.alexallafi.app.domain.usecase

import com.alexallafi.app.domain.HistoryEntry
import com.alexallafi.app.domain.HistoryRepository
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ViewHistoryUseCase(
    private val historyRepository: HistoryRepository,
) : BaseCoroutineUseCase<List<HistoryEntry>, Unit> {
    override suspend fun invoke(vararg params: Any): Result<List<HistoryEntry>, Unit> = Ok(historyRepository.getAll())

    override fun observe(): Flow<Result<List<HistoryEntry>, Unit>> =
        historyRepository
            .observeAll()
            .map { Ok(it) }
}
