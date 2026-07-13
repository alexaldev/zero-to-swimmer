package com.alexallafi.app.presentation.designsystem

import com.alexallafi.app.presentation.nextSession.FavoriteSessionViewItem
import com.alexallafi.app.presentation.nextSession.NextSessionViewItem
import com.alexallafi.app.presentation.trainingProgram.SwimSessionListItem

object PreviewDefaults {
    val favoriteSessionViewItem =
        FavoriteSessionViewItem(
            "1x200, rest for 12 breathes between 200m\n1x200, " +
                "rest for 12 breathes between 200m\n1x200, rest for 12 breathes between 200m",
        )

    val nextSessionViewItem =
        NextSessionViewItem(
            id = "123",
            sessionTitle = "Week 1, Day 1",
            sessionSetsText = "4 x 100m, 4 x 50m, 4 x 25m\n4 x 100m, 4 x 50m, 4 x 25m",
            totalDistanceText = "900m total",
            showConfirmState = true,
        )

    val expandedSwimSessionItem =
        SwimSessionListItem.SwimSessionViewItem(
            id = "123",
            title = "Week 1, Day 1",
            message = "1200m total",
            isExpanded = true,
            isCompleted = true,
            isFavorite = true,
            swimRounds =
                "4 x 100m, 4 x 50m, 4 x 25m\n" +
                    "4 x 100m, 4 x 50m, 4 x 25m\n" +
                    "4 x 100m, 4 x 50m, 4 x 25m\n" +
                    "4 x 100m, 4 x 50m, 4 x 25m",
        )

    val collapsedSwimSessionItem =
        SwimSessionListItem.SwimSessionViewItem(
            id = "123",
            title = "Week 1, Day 1",
            message = "blah",
            isExpanded = false,
            isCompleted = false,
            isFavorite = false,
            swimRounds =
                "4 x 100m, 4 x 50m, 4 x 25m\n" +
                    "4 x 100m, 4 x 50m, 4 x 25m\n" +
                    "4 x 100m, 4 x 50m, 4 x 25m\n" +
                    "4 x 100m, 4 x 50m, 4 x 25m",
        )
}
