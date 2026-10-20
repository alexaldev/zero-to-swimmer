package com.alexallafi.app.domain.usecase

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.alexallafi.app.domain.HistoryEntry
import com.alexallafi.app.domain.HistoryEntryNote
import com.alexallafi.app.domain.HistoryNotesRepository
import com.alexallafi.app.domain.HistoryRepository
import com.github.michaelbull.result.get
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime

class ViewHistoryUseCaseTest {
    private val mockRepository: HistoryRepository = mockk()
    private val mockNotesRepository: HistoryNotesRepository = mockk()
    private lateinit var testUseCase: ViewHistoryUseCase
    private val fakeHistoryEntry = HistoryEntry("2", "1.1", OffsetDateTime.now())
    private val fakeNote = HistoryEntryNote("Some note")

    @BeforeEach
    fun init() {
        testUseCase = ViewHistoryUseCase(mockRepository, mockNotesRepository)
        coEvery { mockRepository.getAll() } returns listOf(fakeHistoryEntry)
        coEvery { mockNotesRepository.getAllNotes() } returns mapOf(fakeHistoryEntry.id to fakeNote)
        every { mockRepository.observeAll() } returns flowOf(listOf(fakeHistoryEntry))
        every { mockNotesRepository.observeAllNotes() } returns flowOf(mapOf(fakeHistoryEntry.id to fakeNote))
    }

    @Test
    fun `returns a result with aggregated history entries and notes`() =
        runTest {
            val result = testUseCase.invoke()

            assertThat(result.isOk).isTrue()
            val items = result.get()
            assertThat(items?.size).isEqualTo(1)
            assertThat(items?.first()?.entry).isEqualTo(fakeHistoryEntry)
            assertThat(items?.first()?.note).isEqualTo(fakeNote)
        }
}
