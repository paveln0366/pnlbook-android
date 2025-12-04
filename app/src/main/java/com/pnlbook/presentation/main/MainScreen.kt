package com.pnlbook.presentation.main

import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShortNavigationBar
import androidx.compose.material3.ShortNavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pnlbook.navigation.MainNavGraph
import com.pnlbook.navigation.MainNavigationItem
import com.pnlbook.navigation.rememberNavigationState
import com.pnlbook.presentation.connections.ConnectionsScreen
import com.pnlbook.presentation.dashboard.DashboardScreen
import com.pnlbook.presentation.settings.SettingsScreen
import com.pnlbook.presentation.trades.TradesScreen

@Composable
fun MainScreen() {
    val viewModel: MainViewModel = viewModel()
    val navigationState = rememberNavigationState()

    Scaffold(
        bottomBar = {
            ShortNavigationBar() {
                val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()

                val items = listOf(
                    MainNavigationItem.ConnectionsContainer,
                    MainNavigationItem.Dashboard,
                    MainNavigationItem.Settings
                )

                items.forEach { item ->
                    val selected = navBackStackEntry?.destination?.hierarchy?.any {
                        it.route == item.screen.route
                    } ?: false
                    ShortNavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (!selected) navigationState.navigateTo(item.screen.route)
                        },
                        icon = {
                            Icon(item.icon, contentDescription = null)
                        },
                        label = {
                            Text(text = stringResource(id = item.title))
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        MainNavGraph(
            navHostController = navigationState.navHostController,
            connectionsScreenContent = {
                ConnectionsScreen(
                    onTradesClickListener = {
                        navigationState.navigateToTrades(it)
                    }
                )
            },
            tradesScreenContent = { connection ->
                TradesScreen(
                    connection = connection,
                    onBackPressed = {
                        navigationState.navHostController.popBackStack()
                    }
                )
            },
            dashboardScreenContent = {
                DashboardScreen()
            },
            settingsScreenContent = {
                SettingsScreen()
            }
        )
    }
}