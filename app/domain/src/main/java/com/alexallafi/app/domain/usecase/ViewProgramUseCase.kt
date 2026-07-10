package com.alexallafi.app.domain.usecase

import com.alexallafi.app.domain.SessionGroup
import com.alexallafi.app.domain.SwimSession
import com.alexallafi.app.domain.SwimSessionsRepository
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ViewProgramUseCase(
    private val swimSessionsRepository: SwimSessionsRepository,
) : BaseCoroutineUseCase<List<SessionGroup>, Unit> {
    override suspend fun invoke(vararg params: Any): Result<List<SessionGroup>, Unit> {
        val result = swimSessionsRepository.getAll()

        if (result.isSuccess) {
            return Ok(mapToGroups(result.getOrThrow()))
        }
        return Err(Unit)
    }

    override fun observe(): Flow<Result<List<SessionGroup>, Unit>> =
        swimSessionsRepository
            .observeAll()
            .map { sessions -> Ok(mapToGroups(sessions)) }

    private fun mapToGroups(sessions: List<SwimSession>): List<SessionGroup> =
        sessions.groupBy { it.week }.map { (week, weekSessions) ->
            SessionGroup(
                week = week,
                sessions = weekSessions,
                completedMeters = weekSessions.filter { it.completed }.sumOf { it.swimSets.sumOf { set -> set.meters * set.count } },
                totalMeters = weekSessions.sumOf { it.swimSets.sumOf { set -> set.meters * set.count } },
            )
        }
}
