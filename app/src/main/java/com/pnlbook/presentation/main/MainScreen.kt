package com.pnlbook.presentation.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShortNavigationBar
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pnlbook.navigation.MainNavGraph
import com.pnlbook.navigation.MainNavigationItem
import com.pnlbook.navigation.rememberNavigationState
import com.pnlbook.presentation.connections.ConnectionsScreen
import com.pnlbook.presentation.statistics.StatisticsScreen
import com.pnlbook.presentation.trades.TradesScreen

@Preview
@Composable
fun MainScreen() {
    val navigationState = rememberNavigationState()

    Scaffold(
        bottomBar = {
            ShortNavigationBar() {
                val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()

                val items = listOf(
                    MainNavigationItem.Connections,
                    MainNavigationItem.Dashboard,
                    MainNavigationItem.Settings
                )

                items.forEach { item ->
                    val selected = navBackStackEntry?.destination?.hierarchy?.any {
                        it.route == item.screen.route
                    } ?: false

                    ShortNavigationBarItem(
                        selected = selected,
                        onClick = { if (!selected) navigationState.navigateTo(item.screen.route) },
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(text = stringResource(id = item.titleResId)) }
                    )
                }
            }
        }
    ) { paddingValues ->
        MainNavGraph(
            navHostController = navigationState.navHostController,
            connectionsListScreenContent = {
                ConnectionsScreen(
                    paddingValues = paddingValues,
                    onTradesClickListener = {
                        navigationState.navigateToTrades(it)
                    },
                    onStatisticClickListener = {
                        navigationState.navigateToStatistics(it)
                    }
                )
            },
            tradesScreenContent = { connection ->
                TradesScreen(
                    onBackPressed = { navigationState.navHostController.popBackStack() },
                    connection = connection
                )
            },
            statisticsScreenContent = { connection ->
                StatisticsScreen(
                    onBackPressed = { navigationState.navHostController.popBackStack() },
                    connection = connection
                )
            },
            dashboardScreenContent = {
                Box(modifier = Modifier.padding(paddingValues)) { TextCounter(name = "Dashboard") }
            },
            settingsScreenContent = {
                Box(modifier = Modifier.padding(paddingValues)) { TextCounter(name = "Settings") }
            }
        )
    }
}

@Composable
private fun TextCounter(name: String) {
    var count by rememberSaveable {
        mutableStateOf(0)
    }
    Text(
        modifier = Modifier.clickable { count++ },
        text = "$name Count: $count",
        color = Color.Black
    )
}