package com.pnlbook.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun MainNavGraph(
    navHostController: NavHostController,
    connectionsScreenContent: @Composable () -> Unit,
    dashboardScreenContent: @Composable () -> Unit,
    settingsScreenContent: @Composable () -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Connections.route
    ) {
        composable(Screen.Connections.route) {
            connectionsScreenContent()
        }
        composable(Screen.Dashboard.route) {
            dashboardScreenContent()
        }
        composable(Screen.Settings.route) {
            settingsScreenContent()
        }
    }
}