package com.alexallafi.app.presentation.nextSession

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.visible
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexallafi.app.presentation.R
import com.alexallafi.app.presentation.designsystem.ZeroToSwimmerTheme

@Composable
fun NextSessionCardRoot(
    viewModel: NextSessionViewModel,
) {
    val item by viewModel.nextViewItem.collectAsState()
    NextSessionCard(
        item = item,
        onMarkCompleted = { viewModel.onAction(UserAction.MarkSessionAsCompleted(item.id)) },
        onConfirmCompletion = { viewModel.onAction(UserAction.ConfirmCompletion) },
        onCancelCompletion = { viewModel.onAction(UserAction.CancelCompletion) }
    )
}

@Composable
fun NextSessionCard(
    item: NextSessionViewItem,
    onMarkCompleted: () -> Unit = {},
    onConfirmCompletion: () -> Unit = {},
    onCancelCompletion: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        border = BorderStroke(width = 0.dp, color = MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {

        Column(
            modifier = modifier.padding(24.dp)
        ) {
            Text(
                text = stringResource(R.string.next_up),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = item.sessionTitle,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = item.totalDistanceText,
                style = MaterialTheme.typography.bodyMedium,
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
                modifier = modifier.padding(top = 16.dp)
            )
            Text(
                text = item.sessionSetsText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = modifier.padding(top = 16.dp)
            )
            Row(
                modifier = modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    modifier = modifier.visible(item.showConfirmState).padding(end = 16.dp),
                    onClick = onCancelCompletion
                ) {
                    Text(
                        text = stringResource(android.R.string.cancel),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                FilledTonalButton(
                    modifier = modifier,
                    onClick = {
                        when {
                            item.showConfirmState -> onMarkCompleted()
                            else -> onConfirmCompletion()
                        }
                    }
                ) {
                    Text(
                        text = if (item.showConfirmState) stringResource(R.string.confirm_completion)
                        else stringResource(R.string.mark_as_completed),
                    )
                }
            }
        }

    }
}

@Preview
@Composable
fun NextSessionCardPreview() {
    ZeroToSwimmerTheme {
        NextSessionCard(
            NextSessionViewItem(
                id = "123",
                sessionTitle = "Week 1, Day 1",
                sessionSetsText = "4 x 100m, 4 x 50m, 4 x 25m\n4 x 100m, 4 x 50m, 4 x 25m",
                totalDistanceText = "900m total",
                showConfirmState = true
            ))
    }
}