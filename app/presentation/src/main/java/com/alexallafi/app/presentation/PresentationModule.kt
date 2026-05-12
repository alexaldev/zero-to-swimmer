package com.alexallafi.app.presentation

import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val presentationModule =
    module {
        viewModelOf(::SessionsViewModel)
        singleOf(::AndroidStringProvider) bind StringResourcesProvider::class
        factory { ViewItemsMapper(get(), get(), get(), includeOverview = true) }
    }
