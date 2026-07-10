package com.alexallafi.app.domain.usecase

import assertk.assertThat
import assertk.assertions.isNotEmpty
import assertk.assertions.isTrue
import com.alexallafi.app.domain.FakesProvider
import com.alexallafi.app.domain.SwimSessionsRepository
import com.github.michaelbull.result.get
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ViewProgramUseCaseTest {
    private val mockRepository: SwimSessionsRepository = mockk()
    private lateinit var testUseCase: ViewProgramUseCase

    @BeforeEach
    fun init() {
        testUseCase = ViewProgramUseCase(mockRepository)
        coEvery { mockRepository.getAll() } returns Result.success(FakesProvider.fakeSessions)
        coEvery { mockRepository.observeAll() } returns flowOf(FakesProvider.fakeSessions)
    }

    @Test
    fun `all program's stored sessions are shown`() =
        runTest {
            val result = testUseCase.invoke()
            assertThat(result.isOk).isTrue()
            assertThat(result.get()!!).isNotEmpty()
        }

    @Test
    fun `observing program returns groups`() =
        runTest {
            val result = testUseCase.observe().first()
            assertThat(result.isOk).isTrue()
            assertThat(result.get()!!).isNotEmpty()
        }
}
