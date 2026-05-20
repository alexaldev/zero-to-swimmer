package com.alexallafi.app.presentation.nextSession

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexallafi.app.domain.ConfigurationRepository
import com.alexallafi.app.domain.SwimSessionsRepository
import com.alexallafi.app.presentation.ViewItemsMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NextSessionViewModel(
    private val swimSessionsRepository: SwimSessionsRepository,
    private val configRepository: ConfigurationRepository,
    private val viewItemsMapper: ViewItemsMapper,
) : ViewModel() {
    private val _favoriteViewItem =
        MutableStateFlow<FavoriteSessionViewItem>(
            FavoriteSessionViewItem(),
        )
    val favoriteViewItem = _favoriteViewItem

    private val _nextViewItem =
        MutableStateFlow<NextSessionViewItem>(
            NextSessionViewItem(),
        )
    val nextViewItem = _nextViewItem

    init {
        viewModelScope.launch {
            setupFavoriteSession()
            setupNextSession()
        }
    }

    private suspend fun setupNextSession() {
        val nextAvailableSession = swimSessionsRepository.getAll().getOrNull()?.firstOrNull { it.completed.not() }
        val nextAvailableText =
            nextAvailableSession?.let { swimSession ->
                val sessionIdText = viewItemsMapper.titleFor(swimSession)
                val setsText =
                    viewItemsMapper.mapSwimRoundsFor(
                        swimSession.swimSets,
                        configRepository.getPoolSize(),
                    )
                "$sessionIdText\n$setsText"
            } ?: "-"

        _nextViewItem.value =
            NextSessionViewItem(
                sessionSetsText = nextAvailableText,
            )
    }

    private suspend fun setupFavoriteSession() {
        val favoriteId = configRepository.getFavoriteSessionId()
        val favoriteText =
            favoriteId?.let {
                swimSessionsRepository.getById(it)?.let { swimSession ->
                    val sessionIdText = viewItemsMapper.titleFor(swimSession)
                    val setsText =
                        viewItemsMapper.mapSwimRoundsFor(
                            swimSession.swimSets,
                            configRepository.getPoolSize(),
                        )
                    "$sessionIdText\n$setsText"
                }
            } ?: "-"

        _favoriteViewItem.value =
            FavoriteSessionViewItem(
                sessionSetsText = favoriteText,
            )
    }
}

data class FavoriteSessionViewItem(
    val sessionSetsText: String = "",
)

data class NextSessionViewItem(
    val sessionSetsText: String = "",
)
