package com.alexallafi.app.presentation

import com.alexallafi.app.domain.SwimSession
import com.alexallafi.app.domain.SwimSessionsRepository
import com.alexallafi.app.domain.SwimmingSet
import com.alexallafi.app.domain.SwimmingWeek

class ViewItemsMapper(
    private val stringResourcesProvider: StringResourcesProvider,
    private val swimSessionsRepository: SwimSessionsRepository
) {

    suspend fun mapToViewItems(swimSessions: List<SwimSession>): List<SwimSessionListItem> {

        return swimSessions
            .groupBy { it.week.value }
            .map {
                buildList<SwimSessionListItem> {
                    add(
                        SwimSessionListItem.HeaderItem(
                            startText = "${stringResourcesProvider.getString(R.string.week)} ${it.key}",
                            endText = "[${swimSessionsRepository.completedMetersForWeek(SwimmingWeek(it.key))}/" +
                                    "${swimSessionsRepository.totalMetersForWeek(SwimmingWeek(it.key))}] " +
                                    stringResourcesProvider.getString(R.string.completed)
                        )
                    )
                    it.value.map { it.toSwimSessionViewItem() }.forEach { sessionViewItem -> add(sessionViewItem) }
                }
            }
            .flatten()

    }

    private fun SwimSession.toSwimSessionViewItem(): SwimSessionListItem.SwimSessionViewItem {
        return SwimSessionListItem.SwimSessionViewItem(
            isCompleted = this.completed,
//            title = "${stringResourcesProvider.getString(R.string.day)} ${this.priority%this.week.value}",
        )
    }

    private fun SwimmingSet.toViewItemEntry(): String {
        return this.toString()
    }
}