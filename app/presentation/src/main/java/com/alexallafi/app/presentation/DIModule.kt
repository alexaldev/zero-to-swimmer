package com.alexallafi.app.presentation

import com.alexallafi.app.presentation.history.HistoryViewModel
import com.alexallafi.app.presentation.nextSession.NextSessionViewModel
import com.alexallafi.app.presentation.settings.SettingsViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val presentationModule =
    module {
        viewModelOf(::SessionsViewModel)
        viewModelOf(::NextSessionViewModel)
        viewModelOf(::HistoryViewModel)
        viewModelOf(::SettingsViewModel)
        singleOf(::AndroidStringProvider) bind StringResourcesProvider::class
        factory { ViewItemsMapper(get(), get(), get(), includeOverview = false) }
    }
