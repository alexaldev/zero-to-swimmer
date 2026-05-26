package com.alexallafi.app.presentation

import com.alexallafi.app.presentation.nextSession.NextSessionViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val presentationModule =
    module {
        viewModelOf(::SessionsViewModel)
        viewModelOf(::NextSessionViewModel)
        singleOf(::AndroidStringProvider) bind StringResourcesProvider::class
        factory { ViewItemsMapper(get(), get(), get(), includeOverview = false) }
    }
