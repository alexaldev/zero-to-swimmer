package com.alexallafi.app.presentation

class FakeStringResourcesProvider : StringResourcesProvider {
    override fun getString(resId: Int): String {
        return when(resId) {
            R.string.week -> "Week"
            R.string.completed -> "Completed"
            R.string.day -> "Day"
            R.string.swim_round_description -> "%d x %d, rest for %d between %d"
            else -> ""
        }
    }

    override fun getString(resId: Int, vararg args: Any): String {
        return "fake"
    }
}