package com.alexallafi.app.presentation.history

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexallafi.app.presentation.designsystem.AppTheme
import com.alexallafi.app.presentation.designsystem.ZeroToSwimmerTheme

@Composable
fun HistoryScreenRoot(viewModel: HistoryViewModel) {
    val items by viewModel.historyItems.collectAsState()
    HistoryScreen(items)
}

@Composable
fun HistoryScreen(
    items: List<HistoryListItem>,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        if (items.isEmpty()) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = "No completed sessions yet")
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(all = AppTheme.spacing.medium),
                verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.medium),
            ) {
                items(
                    items = items,
                    key = { item ->
                        when (item) {
                            is HistoryListItem.MonthHeader -> item.month
                            is HistoryListItem.SessionItem -> item.entryId
                        }
                    },
                ) { item ->
                    when (item) {
                        is HistoryListItem.MonthHeader -> {
                            MonthHeaderItem(item.month)
                        }

                        is HistoryListItem.SessionItem -> {
                            HistorySessionItem(item)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun HistoryScreenPreview() {
    ZeroToSwimmerTheme {
        HistoryScreen(
            listOf(
                HistoryListItem.MonthHeader("January 2023"),
                HistoryListItem.SessionItem(
                    entryId = "1",
                    sessionId = "session_1",
                    sessionTitle = "Week 1 - Day 1",
                    completedAt = "Yesterday at 18:30",
                ),
                HistoryListItem.MonthHeader("February 2023"),
            ),
        )
    }
}

@Composable
fun MonthHeaderItem(
    month: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = month,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.primary,
        modifier =
        modifier,
    )
}

@Composable
fun HistorySessionItem(
    item: HistoryListItem.SessionItem,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(16.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(
            modifier =
                Modifier
                    .padding(AppTheme.spacing.medium),
        ) {
            Text(
                text = item.sessionTitle,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = item.completedAt,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview
@Composable
fun MonthHeaderItemPreview() {
    ZeroToSwimmerTheme {
        MonthHeaderItem("January 2023")
    }
}

@Preview
@Composable
fun HistorySessionItemPreview() {
    ZeroToSwimmerTheme {
        HistorySessionItem(
            item =
                HistoryListItem.SessionItem(
                    entryId = "1",
                    sessionId = "session_1",
                    sessionTitle = "Week 1 - Day 1",
                    completedAt = "Yesterday at 18:30",
                ),
        )
    }
}
