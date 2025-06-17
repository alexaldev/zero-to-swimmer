package com.alexallafi.app.presentation

import androidx.annotation.VisibleForTesting
import com.alexallafi.app.domain.SwimSession
import com.alexallafi.app.domain.SwimSessionsRepository
import com.alexallafi.app.domain.SwimmingSet
import com.alexallafi.app.domain.SwimmingWeek
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

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
            id = this.id,
            title = "${stringResourcesProvider.getString(R.string.day)} ${((this.weekPriority - 1)%(SwimSession.AVAILABLE_WEEK_PRIORITIES.last)) + 1}",
            message = sessionsCompletedMessaged(this),
            isCompleted = this.completed,
            swimRounds = this.swimSets.joinToString("\n") { swimSet ->
                stringResourcesProvider.getString(R.string.swim_round_description).format(swimSet.count, swimSet.meters, swimSet.restBreathsCount, swimSet.meters)
            }

        )
    }

    @VisibleForTesting
    fun sessionsCompletedMessaged(session: SwimSession): String {
        if (session.completed) {
            val zonedDateTime = session.completedAt!!.atZoneSameInstant(ZoneId.systemDefault())
            val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
            return stringResourcesProvider.getString(R.string.completed_at).format(session.completedAt!!.format(formatter))
        }

        return stringResourcesProvider.getString(R.string.meters_total).format(session.swimSets.sumOf { it.meters * it.count })
    }

    private fun SwimmingSet.toViewItemEntry(): String {
        return this.toString()
    }
}