package com.alexallafi.zerotoswimmer

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val homeModule =
    module {
        viewModel { HomeViewModel(get(), get()) }
    }
