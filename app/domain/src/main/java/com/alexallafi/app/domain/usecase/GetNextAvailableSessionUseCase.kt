package com.alexallafi.app.domain.usecase

import com.alexallafi.app.domain.SwimSession
import com.alexallafi.app.domain.SwimSessionsRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNextAvailableSessionUseCase(
    private val swimSessionsRepository: SwimSessionsRepository,
) : BaseCoroutineUseCase<SwimSession, Unit> {
    override suspend fun invoke(vararg params: Any): Result<SwimSession, Unit> =
        swimSessionsRepository
            .getAll()
            .getOrNull()
            ?.first { !it.completed }
            ?.let { Ok(it) }
            ?: Err(Unit)

    override fun observe(): Flow<Result<SwimSession, Unit>> =
        swimSessionsRepository.observeAll().map { sessions ->
            sessions.firstOrNull { !it.completed }?.let { Ok(it) } ?: Err(Unit)
        }
}
