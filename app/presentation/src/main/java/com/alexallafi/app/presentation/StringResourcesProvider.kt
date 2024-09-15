package com.alexallafi.app.presentation

interface StringResourcesProvider {
    fun getString(resId: Int): String
    fun getString(resId: Int, vararg args: Any): String
}