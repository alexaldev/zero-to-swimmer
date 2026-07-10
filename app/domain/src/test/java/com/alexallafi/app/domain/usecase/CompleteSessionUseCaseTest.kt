package com.alexallafi.app.domain.usecase

import com.alexallafi.app.domain.HistoryRepository
import com.alexallafi.app.domain.SwimSession
import com.alexallafi.app.domain.SwimSessionsRepository
import com.alexallafi.app.domain.SwimmingWeek
import com.github.michaelbull.result.get
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CompleteSessionUseCaseTest {
    private val swimSessionsRepository: SwimSessionsRepository = mockk()
    private val historyRepository: HistoryRepository = mockk()
    private lateinit var useCase: CompleteSessionUseCase

    @BeforeEach
    fun setUp() {
        useCase = CompleteSessionUseCase(swimSessionsRepository, historyRepository)
    }

    @Test
    fun `when session exists, toggle completed and add to history`() =
        runTest {
            val sessionId = "1-1"
            val swimSession =
                SwimSession(
                    weekPriority = 1,
                    completed = true,
                    week = SwimmingWeek.FIRST,
                    swimSets = emptyList(),
                    completedAt = null,
                )

            coEvery { swimSessionsRepository.toggleCompleted(sessionId) } returns Unit
            coEvery { swimSessionsRepository.getById(sessionId) } returns swimSession
            coEvery { historyRepository.addSession(swimSession) } returns Unit

            val result = useCase.invoke(sessionId)

            assertNotNull(result.get())
            coVerify { swimSessionsRepository.toggleCompleted(sessionId) }
            coVerify { historyRepository.addSession(swimSession) }
        }

    @Test
    fun `invoking without sessionId should return error`() =
        runTest {
            assertThrows<IllegalArgumentException> {
                useCase.invoke()
            }
        }
}
