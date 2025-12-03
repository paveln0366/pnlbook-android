package com.pnlbook.navigation

import android.net.Uri
import com.google.gson.Gson
import com.pnlbook.domain.Connection

sealed class Screen(
    val route: String
) {
    object Connections : Screen(ROUTE_CONNECTIONS)
    object ConnectionsList : Screen(ROUTE_CONNECTIONS_LIST)
    object Dashboard : Screen(ROUTE_DASHBOARD)
    object Settings : Screen(ROUTE_SETTINGS)
    object Trades : Screen(ROUTE_TRADES) {

        private const val ROUTE_FOR_ARGS = "trades"

        fun getRoutWithArgs(connection: Connection): String {
            val connectionJson = Gson().toJson(connection)
            return "$ROUTE_FOR_ARGS/${connectionJson.encode()}"
        }
    }

    object Statistics : Screen(ROUTE_STATISTICS) {

        private const val ROUTE_FOR_ARGS = "statistics"

        fun getRoutWithArgs(connection: Connection): String {
            val connectionJson = Gson().toJson(connection)
            return "$ROUTE_FOR_ARGS/${connectionJson.encode()}"
        }
    }

    companion object {

        const val KEY_CONNECTION = "connection"

        const val ROUTE_CONNECTIONS = "connections"
        const val ROUTE_CONNECTIONS_LIST = "connections_list"
        const val ROUTE_DASHBOARD = "dashboard"
        const val ROUTE_SETTINGS = "settings"
        const val ROUTE_TRADES = "trades/{$KEY_CONNECTION}"
        const val ROUTE_STATISTICS = "statistics/{$KEY_CONNECTION}"
    }
}

fun String.encode(): String = Uri.encode(this)