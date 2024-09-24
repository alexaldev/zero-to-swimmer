package com.alexallafi.app.presentation

import com.alexallafi.app.domain.SwimSession
import com.alexallafi.app.domain.SwimSessionsRepository
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class SessionsViewModelTests {

    private lateinit var testViewModel: SessionsViewModel
    private val mockRepository: SwimSessionsRepository = mockk()
    private val mockViewItemsMapper: ViewItemsMapper = mockk()
    private val fakeInitialDataPopulator = FakeInitialDataPopulator()

    @Test
    fun onEmptyInitialDataPopulatorGetsCalled() = runTest {

    }
}