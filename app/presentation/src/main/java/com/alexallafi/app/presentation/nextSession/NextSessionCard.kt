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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alexallafi.app.presentation.R
import com.alexallafi.app.presentation.designsystem.AppTheme
import com.alexallafi.app.presentation.designsystem.PreviewDefaults
import com.alexallafi.app.presentation.designsystem.ZeroToSwimmerTheme

@Composable
fun NextSessionCard(
    item: NextSessionViewItem,
    onUserAction: (UserAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        border = BorderStroke(width = 0.dp, color = MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(24.dp)
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
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = item.sessionSetsText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(top = 16.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    modifier = Modifier.visible(item.showConfirmState).padding(end = AppTheme.spacing.medium),
                    onClick = { onUserAction(UserAction.CancelCompletion) }
                ) {
                    Text(
                        text = stringResource(android.R.string.cancel),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                FilledTonalButton(
                    onClick = {
                        onUserAction(when {
                            item.showConfirmState -> UserAction.ConfirmCompletion
                            else -> UserAction.MarkSessionAsCompleted(item.id)
                        })
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
            PreviewDefaults.nextSessionViewItem,
            onUserAction = {})
    }
}