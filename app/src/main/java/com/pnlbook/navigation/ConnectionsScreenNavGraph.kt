package com.pnlbook.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import com.pnlbook.domain.Connection

fun NavGraphBuilder.connectionsScreenNavGraph(
    connectionsListScreenContent: @Composable () -> Unit,
    tradesScreenContent: @Composable (Connection) -> Unit,
    statisticsScreenContent: @Composable (Connection) -> Unit
) {
//    navigation(
//        startDestination = Screen.ConnectionsList.route,
//        route = Screen.Connections.route
//    ) {
//        composable(Screen.ConnectionsList.route) {
//            connectionsListScreenContent()
//        }
//        composable(
//            route = Screen.Trades.route,
//            arguments = listOf(
//                navArgument(Screen.KEY_CONNECTION) { type = Connection.NavigationType }
//            )
//        ) {
//            val connection = it.arguments?.getParcelable<Connection>(Screen.KEY_CONNECTION)
//                ?: throw RuntimeException("Args is null")
//            tradesScreenContent(connection)
//        }
//        composable(
//            route = Screen.Statistics.route,
//            arguments = listOf(
//                navArgument(Screen.KEY_CONNECTION) { type = Connection.NavigationType }
//            )
//        ) {
//            val connection = it.arguments?.getParcelable<Connection>(Screen.KEY_CONNECTION)
//                ?: throw RuntimeException("Args is null")
//            statisticsScreenContent(connection)
//        }
//    }
}