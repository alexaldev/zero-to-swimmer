package com.alexallafi.app.domain.usecase

import com.github.michaelbull.result.Result
import kotlinx.coroutines.flow.Flow

interface BaseUseCase<out T, out E> {
    fun invoke(): Result<T, E>

    fun invoke(vararg params: Any): Result<T, E>
}

interface BaseCoroutineUseCase<out T, out E> {
    suspend fun invoke(vararg params: Any): Result<T, E>

    fun observe(): Flow<Result<T, E>>
}
