package com.alexallafi.app.domain

enum class PoolSize {
    Meters25 {
        override fun toIntValue() = 25
    },
    Meters50 {
        override fun toIntValue() = 50
    };
    abstract fun toIntValue(): Int

    companion object {
        fun fromInt(value: Int): PoolSize {
            return when(value) {
                25 -> Meters25
                50 -> Meters50
                else -> error("Cannot extract PoolSize for value: $value")
            }
        }
    }
}