package com.alexallafi.app.presentation.nextSession

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexallafi.app.presentation.R
import com.alexallafi.app.presentation.designsystem.ZeroToSwimmerTheme

@Composable
fun FavoriteItemScreenRoot(viewModel: NextSessionViewModel) {
    val items by viewModel.favoriteViewItem.collectAsState()
    FavoriteItemScreen(items)
}

@Composable
fun FavoriteItemScreen(
    item: FavoriteSessionViewItem,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(
            modifier = modifier.padding(20.dp),
        ) {
            Text(
                text = stringResource(R.string.favorite_session),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = item.sessionSetsText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = modifier.padding(top = 8.dp),
            )
        }
    }
}

@Preview
@Composable
fun FavoriteItemScreenPreview() {
    ZeroToSwimmerTheme {
        FavoriteItemScreen(
            FavoriteSessionViewItem(
                "1x200, rest for 12 breathes between 200m\n1x200, " +
                    "rest for 12 breathes between 200m\n1x200, rest for 12 breathes between 200m",
            ),
        )
    }
}
