package com.alexallafi.app.presentation.trainingProgram

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexallafi.app.presentation.R
import com.alexallafi.app.presentation.designsystem.Grey
import com.alexallafi.app.presentation.designsystem.PreviewDefaults
import com.alexallafi.app.presentation.designsystem.ZeroToSwimmerTheme

@Composable
fun TrainingSessionExpandedCard(
    item: SwimSessionListItem.SwimSessionViewItem,
    onUserAction: (SwimSessionAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    TrainingSessionContainerCard(
        item = item,
        modifier = modifier
    ) {
        Column(modifier = modifier.padding(16.dp)) {
            SessionItemCardTitle(item, onUserAction, modifier)

            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                modifier = modifier.padding(vertical = 8.dp),
            )
            Text(
                text = item.swimRounds,
                style = MaterialTheme.typography.bodyMedium,
            )
            IconButton(
                onClick = { onUserAction.invoke(SwimSessionAction.FavoriteToggled(item)) },
                modifier = modifier.align(Alignment.End),
            ) {
                Icon(
                    imageVector = if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Collapse Card",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
fun TrainingSessionCollapsedCard(
    item: SwimSessionListItem.SwimSessionViewItem,
    onUserAction: (SwimSessionAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    TrainingSessionContainerCard(
        item = item,
        modifier = modifier
    ) {
        Column(modifier = modifier.padding(16.dp)) {
            SessionItemCardTitle(item, onUserAction, modifier)
        }
    }
}

@Composable
fun SessionItemCardTitle(
    item: SwimSessionListItem.SwimSessionViewItem,
    onUserAction: (SwimSessionAction) -> Unit,
    modifier: Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val iconResource = if (item.isCompleted) R.drawable.ic_check else R.drawable.pool

        Icon(
            painter = painterResource(iconResource),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary,
            modifier = modifier.clip(CircleShape),
        )
        Column(
            modifier =
                modifier
                    .padding(start = 8.dp)
                    .weight(1f),
        ) {
            Text(
                text = item.title,
                fontWeight = Bold,
            )
            Text(
                text = item.message,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        IconButton(
            onClick = {
                onUserAction.invoke(
                    if (item.isExpanded) {
                        SwimSessionAction.CollapseSession(item)
                    } else {
                        SwimSessionAction.ExpandSession(item)
                    },
                )
            },
            modifier = modifier,
        ) {
            Icon(
                imageVector = if (item.isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Collapse Card",
            )
        }
    }
}

@Composable
fun TrainingSessionContainerCard(
    item: SwimSessionListItem.SwimSessionViewItem,
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope) -> Unit,
) {
    val cardContainerColor = if (item.isCompleted) Grey else MaterialTheme.colorScheme.surface

    Card(
        modifier = modifier.padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = cardContainerColor,
            ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        content = content,
    )
}

@Composable
fun WeekHeader(
    item: SwimSessionListItem.WeekHeaderItem,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = item.startText,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = item.endText,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
@Preview
fun WeekHeaderPreview() {
    ZeroToSwimmerTheme {
        WeekHeader(
            PreviewDefaults.weekHeaderSessionItem,
        )
    }
}

@Composable
@Preview
fun TrainingSessionExpandedCardPreview() {
    ZeroToSwimmerTheme {
        TrainingSessionExpandedCard(
            PreviewDefaults.expandedSwimSessionItem,
            {},
        )
    }
}
