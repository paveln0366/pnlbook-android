package com.pnlbook.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CastConnected
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SignalCellularAlt
import androidx.compose.ui.graphics.vector.ImageVector
import com.pnlbook.R

sealed class MainNavigationItem(
    val screen: Screen,
    val titleResId: Int,
    val icon: ImageVector
) {
    object Connections : MainNavigationItem(
        screen = Screen.Connections,
        titleResId = R.string.connections,
        icon = Icons.Outlined.SignalCellularAlt
    )

    object Dashboard : MainNavigationItem(
        screen = Screen.Dashboard,
        titleResId = R.string.dashboard,
        icon = Icons.Outlined.Dashboard
    )

    object Settings : MainNavigationItem(
        screen = Screen.Settings,
        titleResId = R.string.Settings,
        icon = Icons.Outlined.Settings
    )
}