package com.alexallafi.app.domain

interface InitialDataPopulator {
    fun createSessions(): List<SwimSession>
}