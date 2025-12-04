package com.pnlbook.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.google.gson.Gson
import com.pnlbook.domain.Connection

fun NavGraphBuilder.connectionsContainerNavGraph(
    connectionsScreenContent: @Composable () -> Unit,
    tradesScreenContent: @Composable (Connection) -> Unit
) {
    navigation(
        startDestination = Screen.Connections.route,
        route = Screen.ConnectionsContainer.route
    ) {
        composable(Screen.Connections.route) {
            connectionsScreenContent()
        }
        composable(
            route = Screen.Trades.route,
            arguments = listOf(
                navArgument(Screen.KEY_CONNECTION) {
                    type = NavType.StringType
                }
            )
        ) {
            val connectionJson = it.arguments?.getString(Screen.KEY_CONNECTION) ?: ""
            val connection = Gson().fromJson(connectionJson, Connection::class.java)
            tradesScreenContent(connection)
        }
    }
}