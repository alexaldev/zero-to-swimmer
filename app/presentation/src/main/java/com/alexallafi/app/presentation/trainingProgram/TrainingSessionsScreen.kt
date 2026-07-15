package com.alexallafi.app.presentation.trainingProgram

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexallafi.app.presentation.designsystem.PreviewDefaults

@Composable
fun TrainingSessionScreenRoot(viewModel: SessionsViewModel) {
    val items by viewModel.sessionsViewItems.collectAsState()

    TrainingSessionScreen(
        items = items,
        onUserAction = viewModel::onAction,
        modifier = Modifier,
    )
}

@Composable
fun TrainingSessionScreen(
    items: List<SwimSessionListItem>,
    modifier: Modifier = Modifier,
    onUserAction: (SwimSessionAction) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
    ) {
        items(
            items = items,
            key = { item ->
                when (item) {
                    is SwimSessionListItem.ProgressOverviewViewItem -> item.hashCode()
                    is SwimSessionListItem.SwimSessionViewItem -> item.id
                    is SwimSessionListItem.WeekHeaderItem -> item.hashCode()
                }
            },
        ) { item ->
            when (item) {
                is SwimSessionListItem.ProgressOverviewViewItem -> {
                    Unit
                }

                is SwimSessionListItem.SwimSessionViewItem -> {
                    if (item.isExpanded) {
                        TrainingSessionExpandedCard(item, onUserAction)
                    } else {
                        TrainingSessionCollapsedCard(item, onUserAction)
                    }
                }

                is SwimSessionListItem.WeekHeaderItem -> {
                    WeekHeader(item, Modifier.padding(16.dp))
                }
            }
        }
    }
}

@Composable
@Preview
fun TrainingSessionScreenPreview() {
    MaterialTheme {
        TrainingSessionScreen(
            PreviewDefaults.trainingSessionsItemsList,
        )
    }
}
