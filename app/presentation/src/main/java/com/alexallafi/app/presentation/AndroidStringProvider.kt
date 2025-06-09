package com.alexallafi.app.presentation

import android.content.Context

class AndroidStringProvider(
    private val context: Context
): StringResourcesProvider {
    override fun getString(resId: Int): String {
        return context.getString(resId)
    }

    override fun getString(resId: Int, vararg args: Any): String {
        return context.getString(resId, args)
    }
}