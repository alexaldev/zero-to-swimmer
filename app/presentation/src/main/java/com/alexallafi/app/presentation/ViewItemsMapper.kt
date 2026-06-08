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
import java.time.OffsetDateTime
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
    private val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)

    fun mapToViewItems(swimSessionsFlow: Flow<List<SwimSession>>) = swimSessionsFlow.map { mapToViewItems(it) }

    suspend fun mapToViewItems(
        swimSessions: List<SwimSession>,
        expandedIds: Set<String> = emptySet(),
    ): List<SwimSessionListItem> {
        val result = mutableListOf<SwimSessionListItem>()

        if (includeOverview) getOverview(swimSessions)?.let { result += it }

        val poolSize = configurationRepository.getPoolSize()
        val favoriteId = configurationRepository.getFavoriteSessionId()

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

                session.value
                    .map {
                        toSwimSessionViewItem(
                            it,
                            expandedIds,
                            poolSize,
                            favoriteId,
                        )
                    }.forEach { sessionViewItem -> result += sessionViewItem }
            }

        return result
    }

    fun getOverview(sessions: List<SwimSession>): SwimSessionListItem? {
        val completedSessions = sessions.count { it.completed }
        val nextAvailable = sessions.firstOrNull { it.completed.not() } ?: return null

        val totalCompletedText = "$completedSessions/${sessions.size}"
        val nextAvailableText = "${stringResourcesProvider.getString(R.string.week)} ${nextAvailable.week.value}, ${
            stringResourcesProvider.getString(
                R.string.day,
            )
        } ${nextAvailable.weekPriority}"

        return SwimSessionListItem.ProgressOverviewViewItem(
            totalCompleted = totalCompletedText,
            nextAvailable = nextAvailableText,
        )
    }

    @VisibleForTesting
    fun toSwimSessionViewItem(
        session: SwimSession,
        expandedIds: Set<String> = emptySet(),
        poolSize: PoolSize,
        favoriteId: String?,
    ): SwimSessionListItem.SwimSessionViewItem =
        SwimSessionListItem.SwimSessionViewItem(
            id = session.id,
            title = dayOnlyTitleFor(session),
            message = sessionsCompletedMessaged(session),
            isCompleted = session.completed,
            isFavorite = session.id == favoriteId,
            isExpanded = expandedIds.contains(session.id),
            swimRounds = mapSwimRoundsFor(session.swimSets, poolSize),
        )

    fun weekAndDayTitleFor(session: SwimSession): String =
        "${stringResourcesProvider.getString(R.string.week)} ${session.week.value}, ${
            stringResourcesProvider.getString(
                R.string.day,
            )
        } ${session.weekPriority}"

    fun dayOnlyTitleFor(session: SwimSession): String =
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

    fun totalDistanceForSession(session: SwimSession): String =
        stringResourcesProvider.getString(R.string.meters_total).format(
            session.swimSets.sumOf {
                it.meters * it.count
            },
        )

    fun completedAtMessage(session: SwimSession): String =
        stringResourcesProvider.getString(R.string.completed_at).format(session.completedAt!!.format(dateFormatter))

    fun completedAtMessage(date: OffsetDateTime): String =
        stringResourcesProvider.getString(R.string.completed_at).format(date.format(dateFormatter))

    @VisibleForTesting
    fun sessionsCompletedMessaged(session: SwimSession): String =
        when {
            session.completed -> {
                require(session.completedAt != null) { "Session cannot be completed and have null completedAt property" }
                completedAtMessage(session)
            }

            else -> {
                totalDistanceForSession(session)
            }
        }
}
