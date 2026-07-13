package com.alexallafi.app.presentation.designsystem

import com.alexallafi.app.presentation.nextSession.FavoriteSessionViewItem
import com.alexallafi.app.presentation.nextSession.NextSessionViewItem

object PreviewDefaults {
    val favoriteSessionViewItem = FavoriteSessionViewItem(
        "1x200, rest for 12 breathes between 200m\n1x200, " +
                "rest for 12 breathes between 200m\n1x200, rest for 12 breathes between 200m",
    )

    val nextSessionViewItem = NextSessionViewItem(
        id = "123",
        sessionTitle = "Week 1, Day 1",
        sessionSetsText = "4 x 100m, 4 x 50m, 4 x 25m\n4 x 100m, 4 x 50m, 4 x 25m",
        totalDistanceText = "900m total",
        showConfirmState = true
    )
}