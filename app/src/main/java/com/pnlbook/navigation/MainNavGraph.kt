package com.pnlbook.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pnlbook.domain.Connection

@Composable
fun MainNavGraph(
    navHostController: NavHostController,
    connectionsScreenContent: @Composable () -> Unit,
    tradesScreenContent: @Composable (Connection) -> Unit,
    dashboardScreenContent: @Composable () -> Unit,
    settingsScreenContent: @Composable () -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.ConnectionsContainer.route
    ) {
        connectionsContainerNavGraph(
            connectionsScreenContent = connectionsScreenContent,
            tradesScreenContent = tradesScreenContent
        )
        composable(Screen.Dashboard.route) {
            dashboardScreenContent()
        }
        composable(Screen.Settings.route) {
            settingsScreenContent()
        }
    }
}