package com.alexallafi.app.domain.usecase

import com.alexallafi.app.domain.ConfigurationRepository
import com.alexallafi.app.domain.SwimSession
import com.alexallafi.app.domain.SwimSessionsRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class SeeFavoriteSessionUseCase(
    private val configurationRepository: ConfigurationRepository,
    private val swimSessionsRepository: SwimSessionsRepository,
) : BaseCoroutineUseCase<SwimSession, Unit> {
    override suspend fun invoke(vararg params: Any): Result<SwimSession, Unit> {
        val favoriteId = configurationRepository.getFavoriteSessionId() ?: return Err(Unit)
        val result = swimSessionsRepository.getById(favoriteId) ?: return Err(Unit)
        return Ok(result)
    }

    override fun observe(): Flow<Result<SwimSession, Unit>> =
        combine(
            configurationRepository.observeFavoriteSession(),
            swimSessionsRepository.observeAll(),
        ) { favoriteId, sessions ->
            val favoriteSession = sessions.find { it.id == favoriteId }
            when (favoriteSession) {
                null -> Err(Unit)
                else -> Ok(favoriteSession)
            }
        }
}
