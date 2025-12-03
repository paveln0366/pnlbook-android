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
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pnlbook.navigation.MainNavGraph
import com.pnlbook.navigation.MainNavigationItem
import com.pnlbook.navigation.rememberNavigationState
import com.pnlbook.presentation.connections.ConnectionsScreen
import com.pnlbook.presentation.connections.ConnectionsViewModel
import com.pnlbook.presentation.dashboard.DashboardScreen
import com.pnlbook.presentation.dashboard.DashboardViewModel
import com.pnlbook.presentation.settings.SettingsScreen
import com.pnlbook.presentation.settings.SettingsViewModel

@Composable
fun MainScreen() {
    val connectionViewModel: ConnectionsViewModel = viewModel()
    val dashboardViewModel: DashboardViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()

    val navigationState = rememberNavigationState()

    Scaffold(
        bottomBar = {
            ShortNavigationBar() {
                val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val items = listOf(
                    MainNavigationItem.Connections,
                    MainNavigationItem.Dashboard,
                    MainNavigationItem.Settings
                )

                items.forEach { item ->
                    ShortNavigationBarItem(
                        selected = currentRoute == item.screen.route,
                        onClick = {
                            navigationState.navigateTo(item.screen.route)
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
                ConnectionsScreen(connectionViewModel)
            },
            dashboardScreenContent = {
                DashboardScreen(dashboardViewModel)
            },
            settingsScreenContent = {
                SettingsScreen(settingsViewModel)
            }
        )
    }
}