package com.alexallafi.app.presentation

import androidx.annotation.VisibleForTesting
import com.alexallafi.app.domain.SwimSession
import com.alexallafi.app.domain.SwimSessionsRepository
import com.alexallafi.app.domain.SwimmingSet
import com.alexallafi.app.domain.SwimmingWeek
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.collections.flatten

class ViewItemsMapper(
    private val stringResourcesProvider: StringResourcesProvider,
    private val swimSessionsRepository: SwimSessionsRepository,
    private val includeOverview: Boolean = true
) {

    suspend fun mapToViewItems(swimSessions: List<SwimSession>): List<SwimSessionListItem> {

        val result = mutableListOf<SwimSessionListItem>()

        if (includeOverview) getOverview(swimSessions)?.let { result += it }

        swimSessions
            .groupBy { it.week.value }
            .map { session ->

                result += SwimSessionListItem.WeekHeaderItem(
                    startText = "${stringResourcesProvider.getString(R.string.week)} ${session.key}",
                    endText = "[${swimSessionsRepository.completedMetersForWeek(SwimmingWeek(session.key))}m/" +
                            "${swimSessionsRepository.totalMetersForWeek(SwimmingWeek(session.key))}m] " +
                            stringResourcesProvider.getString(R.string.completed)
                )

                session.value.map { it.toSwimSessionViewItem() }.forEach { sessionViewItem -> result += sessionViewItem }
            }

        return result
    }

    private fun getOverview(sessions: List<SwimSession>): SwimSessionListItem? {

        val allSessions = sessions
        val completedSessions = allSessions.count { it.completed }
        val nextAvailable = allSessions.firstOrNull { it.completed.not()  } ?: return null

        val totalCompletedText = "$completedSessions/${allSessions.size}"
        val nextAvailableText = "Week ${nextAvailable.week.value}, Day ${nextAvailable.weekPriority}"

        return SwimSessionListItem.ProgressOverviewViewItem(
            totalCompleted = totalCompletedText,
            nextAvailable = nextAvailableText
        )
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