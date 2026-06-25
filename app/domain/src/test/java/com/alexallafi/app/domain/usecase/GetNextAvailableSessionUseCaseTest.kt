package com.alexallafi.app.domain.usecase

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.alexallafi.app.domain.SwimSession
import com.alexallafi.app.domain.SwimSessionsRepository
import com.alexallafi.app.domain.SwimmingWeek
import com.github.michaelbull.result.get
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetNextAvailableSessionUseCaseTest {
    private val mockRepository: SwimSessionsRepository = mockk()
    private lateinit var testUseCase: GetNextAvailableSessionUseCase
    private val fakeCompletedSession = SwimSession(1, true, SwimmingWeek.FIRST, emptyList(), null)
    val fakeIncompleteSession = SwimSession(2, false, SwimmingWeek.FIRST, emptyList(), null)

    @BeforeEach
    fun init() {
        testUseCase = GetNextAvailableSessionUseCase(mockRepository)
        coEvery { mockRepository.getAll() } returns Result.success(listOf(fakeCompletedSession, fakeIncompleteSession))
    }

    @Test
    fun `returns the first not completed session from repository`() =
        runTest {
            val result = testUseCase.invoke()

            assertThat(result.isOk).isTrue()
            assertThat(result.get()).isEqualTo(fakeIncompleteSession)
        }
}
