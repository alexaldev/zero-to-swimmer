package com.alexallafi.app.domain.usecase

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.alexallafi.app.domain.HistoryEntry
import com.alexallafi.app.domain.HistoryRepository
import com.github.michaelbull.result.get
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime

class ViewHistoryUseCaseTest {
    private val mockRepository: HistoryRepository = mockk()
    private lateinit var testUseCase: ViewHistoryUseCase
    private val fakeHistoryEntry = HistoryEntry("2", "1.1", OffsetDateTime.now())

    @BeforeEach
    fun init() {
        testUseCase = ViewHistoryUseCase(mockRepository)
        coEvery { mockRepository.getAll() } returns List(20) { fakeHistoryEntry }
    }

    @Test
    fun `returns a result with all history entries`() =
        runTest {
            val result = testUseCase.invoke()

            assertThat(result.isOk).isTrue()
            assertThat(result.get()).isEqualTo(List(20) { fakeHistoryEntry })
        }
}
