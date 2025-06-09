package com.alexallafi.zerotoswimmer

import android.app.Application
import com.alexallafi.app.data.di.coroutinesModule
import com.alexallafi.app.data.di.localStorageModule
import com.alexallafi.app.presentation.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

class ZeroToSwimmer : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ZeroToSwimmer)
            modules(koinModules())
        }
    }

    companion object {
        fun koinModules(): List<Module> {
            return listOf(
                localStorageModule, presentationModule, coroutinesModule
            )
        }
    }
}