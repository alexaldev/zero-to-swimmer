package com.alexallafi.app.domain.usecase

import com.alexallafi.app.domain.HistoryRepository
import com.alexallafi.app.domain.SwimSessionsRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class CompleteSessionUseCase(
    private val swimSessionsRepository: SwimSessionsRepository,
    private val historyRepository: HistoryRepository,
) : BaseCoroutineUseCase<Unit, Unit> {
    override suspend fun invoke(vararg params: Any): Result<Unit, Unit> {

        require(params.isNotEmpty() && params.first() is String) { "You must provide sessionId as parameter" }

        val sessionId = params.getOrNull(0) as? String ?: return Err(Unit)

        swimSessionsRepository.toggleCompleted(sessionId)
        val swimSession = swimSessionsRepository.getById(sessionId)
        if (swimSession != null) {
            historyRepository.addSession(swimSession)
            return Ok(Unit)
        }
        return Err(Unit)
    }

    override fun observe(): Flow<Result<Unit, Unit>> = emptyFlow()
}
