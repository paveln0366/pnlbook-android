package com.pnlbook.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.pnlbook.R

sealed class MainNavigationItem(
    val screen: Screen,
    val title: Int,
    val icon: ImageVector
) {
    object ConnectionsContainer : MainNavigationItem(
        screen = Screen.ConnectionsContainer,
        title = R.string.connections,
        icon = Icons.Default.NetworkCheck
    )

    object Dashboard : MainNavigationItem(
        screen = Screen.Dashboard,
        title = R.string.dashboard,
        icon = Icons.Default.Dashboard
    )

    object Settings : MainNavigationItem(
        screen = Screen.Settings,
        title = R.string.settings,
        icon = Icons.Default.Settings
    )
}