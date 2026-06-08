package com.alexallafi.app.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexallafi.app.domain.HistoryRepository
import com.alexallafi.app.domain.SwimSessionsRepository
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val historyRepository: HistoryRepository,
    private val swimSessionsRepository: SwimSessionsRepository,
) : ViewModel() {
    fun onAction(userAction: UserAction) {
        when (userAction) {
            UserAction.ClearData -> {
                viewModelScope.launch {
                    historyRepository.clearAll()
                    swimSessionsRepository.clearAll()
                }
            }
        }
    }
}

sealed interface UserAction {
    object ClearData : UserAction
}
