package com.alexallafi.app.presentation

import androidx.annotation.VisibleForTesting
import com.alexallafi.app.domain.ConfigurationRepository
import com.alexallafi.app.domain.PoolSize
import com.alexallafi.app.domain.SwimSession
import com.alexallafi.app.domain.SwimSessionsRepository
import com.alexallafi.app.domain.SwimmingSet
import com.alexallafi.app.domain.SwimmingWeek
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class ViewItemsMapper(
    private val stringResourcesProvider: StringResourcesProvider,
    private val swimSessionsRepository: SwimSessionsRepository,
    private val configurationRepository: ConfigurationRepository,
    /**
     * Flag to enable/disable the overview item when mapping
     */
    private val includeOverview: Boolean = true,
) {
    fun mapToViewItems(swimSessionsFlow: Flow<List<SwimSession>>) = swimSessionsFlow.map { mapToViewItems(it) }

    suspend fun mapToViewItems(
        swimSessions: List<SwimSession>,
        expandedIds: Set<String> = emptySet(),
    ): List<SwimSessionListItem> {
        val result = mutableListOf<SwimSessionListItem>()

        if (includeOverview) getOverview(swimSessions)?.let { result += it }

        swimSessions
            .groupBy { it.week.value }
            .map { session ->

                result +=
                    SwimSessionListItem.WeekHeaderItem(
                        startText = "${stringResourcesProvider.getString(R.string.week)} ${session.key}",
                        endText =
                            "[${swimSessionsRepository.completedMetersForWeek(SwimmingWeek(session.key))}m/" +
                                "${swimSessionsRepository.totalMetersForWeek(SwimmingWeek(session.key))}m] " +
                                stringResourcesProvider.getString(R.string.completed),
                    )

                session.value.map { toSwimSessionViewItem(it, expandedIds) }.forEach { sessionViewItem -> result += sessionViewItem }
            }

        return result
    }

    fun getOverview(sessions: List<SwimSession>): SwimSessionListItem? {
        val completedSessions = sessions.count { it.completed }
        val nextAvailable = sessions.firstOrNull { it.completed.not() } ?: return null

        val totalCompletedText = "$completedSessions/${sessions.size}"
        val nextAvailableText = "Week ${nextAvailable.week.value}, Day ${nextAvailable.weekPriority}"

        return SwimSessionListItem.ProgressOverviewViewItem(
            totalCompleted = totalCompletedText,
            nextAvailable = nextAvailableText,
        )
    }

    suspend fun toSwimSessionViewItem(
        session: SwimSession,
        expandedIds: Set<String> = emptySet(),
    ): SwimSessionListItem.SwimSessionViewItem {
        val poolSize = configurationRepository.getPoolSize()
        val favoriteId = configurationRepository.getFavoriteSessionId()

        return SwimSessionListItem.SwimSessionViewItem(
            id = session.id,
            title = titleFor(session),
            message = sessionsCompletedMessaged(session),
            isCompleted = session.completed,
            isFavorite = session.id == favoriteId,
            isExpanded = expandedIds.contains(session.id),
            swimRounds = mapSwimRoundsFor(session.swimSets, poolSize),
        )
    }

    fun titleFor(session: SwimSession): String =
        "${stringResourcesProvider.getString(
            R.string.day,
        )} ${((session.weekPriority - 1) % (SwimSession.AVAILABLE_WEEK_PRIORITIES.last)) + 1}"

    fun mapSwimRoundsFor(
        sets: List<SwimmingSet>,
        poolSize: PoolSize,
    ): String {
        return when (poolSize) {
            PoolSize.Meters25 -> {
                sets.joinToString("\n") { swimSet ->
                    stringResourcesProvider
                        .getString(
                            R.string.swim_round_description,
                        ).format(swimSet.count, swimSet.meters, swimSet.restBreathsCount, swimSet.meters)
                }
            }

            PoolSize.Meters50 -> {
                // Contract says that
                val setsOf25 = sets.filter { it.meters == PoolSize.Meters25.toIntValue() }

                // x * 25 => (x/2) * 50
                val setsOf25As50 =
                    setsOf25.map {
                        SwimmingSet(meters = PoolSize.Meters50.toIntValue(), count = it.count / 2, restBreathsCount = it.restBreathsCount)
                    }

                return mapSwimRoundsFor((sets - setsOf25) + setsOf25As50, PoolSize.Meters25)
            }
        }
    }

    @VisibleForTesting
    fun sessionsCompletedMessaged(session: SwimSession): String {
        if (session.completed) {
            val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
            return stringResourcesProvider.getString(R.string.completed_at).format(session.completedAt!!.format(formatter))
        }

        return stringResourcesProvider.getString(R.string.meters_total).format(session.swimSets.sumOf { it.meters * it.count })
    }

    private fun SwimmingSet.toViewItemEntry(): String = this.toString()
}
