package com.alexallafi.app.presentation.nextSession

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexallafi.app.domain.ConfigurationRepository
import com.alexallafi.app.domain.SwimSessionsRepository
import com.alexallafi.app.presentation.SwimSessionListItem
import com.alexallafi.app.presentation.ViewItemsMapper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class NextSessionViewModel(
    private val swimSessionsRepository: SwimSessionsRepository,
    private val configRepository: ConfigurationRepository,
    private val viewItemsMapper: ViewItemsMapper,
) : ViewModel() {

    val favoriteViewItem: StateFlow<FavoriteSessionViewItem> =
        combine(
            swimSessionsRepository.observeAll(),
            configRepository.observeFavoriteSession()
        ) { sessions, favoriteId ->
            val favoriteSession = sessions.find { it.id == favoriteId }
            val favoriteText = favoriteSession?.let {
                val sessionIdText = viewItemsMapper.titleFor(it)
                val setsText = viewItemsMapper.mapSwimRoundsFor(it.swimSets, configRepository.getPoolSize())
                "$sessionIdText\n$setsText"
            } ?: "-"
            FavoriteSessionViewItem(favoriteText)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FavoriteSessionViewItem("-"))

    val nextViewItem: StateFlow<NextSessionViewItem> =
        swimSessionsRepository.observeAll().map { sessions ->
            val nextAvailableSession = sessions.firstOrNull { !it.completed }
            val nextAvailableText = nextAvailableSession?.let {
                viewItemsMapper.mapSwimRoundsFor(it.swimSets, configRepository.getPoolSize())
            } ?: "-"
            NextSessionViewItem(nextAvailableText)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NextSessionViewItem("-"))

    val overviewViewItem: StateFlow<SwimSessionListItem.ProgressOverviewViewItem?> =
        swimSessionsRepository.observeAll().map {
            viewItemsMapper.getOverview(it) as? SwimSessionListItem.ProgressOverviewViewItem
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}

data class FavoriteSessionViewItem(
    val sessionSetsText: String = "",
)

data class NextSessionViewItem(
    val sessionSetsText: String = "",
)
