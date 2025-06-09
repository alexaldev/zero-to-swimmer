package com.alexallafi.app.data.di

import com.alexallafi.app.data.local_storage.DefaultInitialDataPopulator
import com.alexallafi.app.data.local_storage.LocalSwimSessionsRepository
import com.alexallafi.app.domain.InitialDataPopulator
import com.alexallafi.app.domain.SwimSessionsRepository
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val localStorageModule = module {
    single<SwimSessionsRepository> { LocalSwimSessionsRepository(get(), get(named("IO"))) }
    singleOf(::DefaultInitialDataPopulator) bind InitialDataPopulator::class
}

val coroutinesModule = module {
    single(named("IO")) { Dispatchers.IO }
    single(named("UI")) { Dispatchers.Main }
    single(named("Default")) { Dispatchers.Default }
}