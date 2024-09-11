package com.alexallafi.app.data.di

import com.alexallafi.app.data.local_storage.LocalSwimSessionsRepository
import com.alexallafi.app.domain.SwimSessionsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val localStorageModule = module {
    singleOf(::LocalSwimSessionsRepository) bind SwimSessionsRepository::class
}