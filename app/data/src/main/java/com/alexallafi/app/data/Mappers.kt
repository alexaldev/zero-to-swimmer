package com.alexallafi.app.data

import com.alexallafi.app.domain.SwimSession
import com.alexallafi.app.domain.SwimmingSet
import com.alexallafi.app.domain.SwimmingWeek

fun List<SwimSession>.toDataModel(): List<com.alexallafi.app.data.local_storage.SwimSession> {
    return this.map {
        com.alexallafi.app.data.local_storage.SwimSession(
            priority = it.priority,
            completed = it.completed,
            week = it.week.value,
            swimSets = it.swimSets.map { domainSet -> domainSet.toDataModel() }
        )
    }
}

fun SwimmingSet.toDataModel(): com.alexallafi.app.data.local_storage.SwimmingSet {
    return com.alexallafi.app.data.local_storage.SwimmingSet(
        meters = meters,
        count = count,
        restBreathsCount = restBreathsCount
    )
}

fun com.alexallafi.app.data.local_storage.SwimmingSet.toDomainModel(): SwimmingSet {
    return SwimmingSet(meters, count, restBreathsCount)
}

fun List<com.alexallafi.app.data.local_storage.SwimSession>.toDomainModel(): List<SwimSession> {
    return this.map {
        SwimSession(
            priority = it.priority,
            completed = it.completed,
            week = SwimmingWeek(it.week),
            swimSets = it.swimSets.map { domainSet -> domainSet.toDomainModel() }
        )
    }
}