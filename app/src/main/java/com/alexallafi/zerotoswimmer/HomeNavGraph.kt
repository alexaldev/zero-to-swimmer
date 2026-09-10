package com.alexallafi.zerotoswimmer

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.alexallafi.app.presentation.history.HistoryScreenRoot
import com.alexallafi.app.presentation.history.HistoryViewModel
import com.alexallafi.app.presentation.navigation.TrainingNavKey
import com.alexallafi.app.presentation.nextSession.NextSessionScreenRoot
import com.alexallafi.app.presentation.nextSession.NextSessionViewModel
import com.alexallafi.app.presentation.settings.SettingsScreenRoot
import com.alexallafi.app.presentation.settings.SettingsViewModel
import com.alexallafi.app.presentation.trainingProgram.SessionsViewModel
import com.alexallafi.app.presentation.trainingProgram.TrainingSessionScreenRoot
import org.koin.androidx.compose.koinViewModel
import com.alexallafi.app.presentation.R as PresentationR

data class HomeNavigationItem(
    val route: TrainingNavKey,
    val labelRes: Int,
    val iconRes: Int,
)

val homeNavigationItems =
    listOf(
        HomeNavigationItem(
            route = TrainingNavKey.NextSession,
            labelRes = R.string.next_session,
            iconRes = PresentationR.drawable.pool,
        ),
        HomeNavigationItem(
            route = TrainingNavKey.SessionsList,
            labelRes = R.string.swim_sessions,
            iconRes = PresentationR.drawable.ic_list,
        ),
        HomeNavigationItem(
            route = TrainingNavKey.History,
            labelRes = R.string.history,
            iconRes = PresentationR.drawable.ic_history,
        ),
        HomeNavigationItem(
            route = TrainingNavKey.Settings,
            labelRes = R.string.settings,
            iconRes = R.drawable.ic_settings,
        ),
    )

fun NavGraphBuilder.homeGraph() {
    composable<TrainingNavKey.NextSession> {
        val viewModel: NextSessionViewModel = koinViewModel()
        NextSessionScreenRoot(viewModel)
    }
    composable<TrainingNavKey.SessionsList> {
        val viewModel: SessionsViewModel = koinViewModel()
        TrainingSessionScreenRoot(viewModel)
    }
    composable<TrainingNavKey.History> {
        val viewModel: HistoryViewModel = koinViewModel()
        HistoryScreenRoot(viewModel)
    }
    composable<TrainingNavKey.Settings> {
        val viewModel: SettingsViewModel = koinViewModel()
        SettingsScreenRoot(viewModel)
    }
}
