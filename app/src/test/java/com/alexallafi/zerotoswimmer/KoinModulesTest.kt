package com.alexallafi.zerotoswimmer

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.alexallafi.app.domain.ConfigurationRepository
import com.alexallafi.app.domain.HistoryNotesRepository
import com.alexallafi.app.domain.HistoryRepository
import com.alexallafi.app.domain.InitialDataPopulator
import com.alexallafi.app.domain.SwimSessionsRepository
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.KoinTest
import org.koin.test.verify.verify

class KoinModulesTest : KoinTest {
    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun checkDefinitions() {
        val externalTypes =
            listOf(
                SwimSessionsRepository::class,
                HistoryRepository::class,
                HistoryNotesRepository::class,
                ConfigurationRepository::class,
                InitialDataPopulator::class,
                SavedStateHandle::class,
                Context::class,
            )

        ZeroToSwimmer.koinModules().forEach {
            it.verify(extraTypes = externalTypes)
        }
    }
}
