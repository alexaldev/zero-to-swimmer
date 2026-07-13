package com.alexallafi.app.presentation.nextSession

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexallafi.app.presentation.designsystem.PreviewDefaults
import com.alexallafi.app.presentation.designsystem.ZeroToSwimmerTheme

@Composable
fun NextSessionScreenRoot(
    viewModel: NextSessionViewModel,
    modifier: Modifier = Modifier,
) {
    val favoriteItem by viewModel.favoriteViewItem.collectAsState()
    val nextSessionItem by viewModel.nextViewItem.collectAsState()

    NextSessionScreen(
        favoriteItem,
        nextSessionItem,
        viewModel::onAction,
        modifier,
    )
}

@Composable
fun NextSessionScreen(
    favoriteItem: FavoriteSessionViewItem,
    nextSessionItem: NextSessionViewItem,
    onUserAction: (UserAction) -> Unit,
    modifier: Modifier,
) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center) {
        FavoriteSessionCard(favoriteItem)
        HorizontalDivider(modifier = modifier.padding(vertical = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
        NextSessionCard(nextSessionItem, onUserAction)
    }
}

@Preview
@Composable
fun NextSessionScreenPreview() {
    ZeroToSwimmerTheme {
        NextSessionScreen(
            favoriteItem = PreviewDefaults.favoriteSessionViewItem,
            nextSessionItem = PreviewDefaults.nextSessionViewItem,
            onUserAction = {},
            Modifier
        )
    }
}
