package com.alexallafi.zerotoswimmer

import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.KoinTest
import org.koin.test.verify.verify

class KoinModulesTest : KoinTest {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun checkDefinitions() {
        ZeroToSwimmer.koinModules().forEach {
            it.verify()
        }
    }
}