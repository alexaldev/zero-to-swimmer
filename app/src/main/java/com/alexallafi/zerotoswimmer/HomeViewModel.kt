package com.alexallafi.zerotoswimmer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexallafi.app.domain.HistoryRepository
import com.alexallafi.app.domain.InitialDataPopulator
import com.alexallafi.app.domain.SwimSessionsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val initialDataPopulator: InitialDataPopulator,
    private val swimmingRepository: SwimSessionsRepository,
    private val historyRepository: HistoryRepository,
) : ViewModel() {
    private val _initializingData = MutableStateFlow<Boolean>(true)
    val initializingData = _initializingData.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {

            val allSessionsResult = swimmingRepository.getAll()
            if (allSessionsResult.isFailure) {
                swimmingRepository.addAll(initialDataPopulator.createSessions())
                _initializingData.update { false }
            } else {
                if (historyRepository.getAll().isEmpty()) {
                    historyRepository.addSessions(
                        allSessionsResult
                            .getOrThrow()
                            .filter { it.completed },
                    )
                    _initializingData.update { false }
                }
                _initializingData.update { false }
            }
        }
    }
}
