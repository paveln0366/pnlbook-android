package com.pnlbook.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pnlbook.domain.Connection

@Composable
fun MainNavGraph(
    navHostController: NavHostController,
    connectionsListScreenContent: @Composable () -> Unit,
    tradesScreenContent: @Composable (Connection) -> Unit,
    statisticsScreenContent: @Composable (Connection) -> Unit,
    dashboardScreenContent: @Composable () -> Unit,
    settingsScreenContent: @Composable () -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Connections.route
    ) {
        connectionsScreenNavGraph(
            connectionsListScreenContent = connectionsListScreenContent,
            tradesScreenContent = tradesScreenContent,
            statisticsScreenContent = statisticsScreenContent
        )
        composable(Screen.Dashboard.route) {
            dashboardScreenContent()
        }
        composable(Screen.Settings.route) {
            settingsScreenContent()
        }
    }
}