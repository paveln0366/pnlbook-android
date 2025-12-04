package com.pnlbook.navigation

import android.net.Uri
import com.google.gson.Gson
import com.pnlbook.domain.Connection

sealed class Screen(
    val route: String
) {
    object ConnectionsContainer : Screen(ROUTE_CONNECTIONS_CONTAINER)
    object Connections : Screen(ROUTE_CONNECTIONS)
    object Trades : Screen(ROUTE_TRADES) {

        private const val ROUT_FOR_ARGS = "trades"

        fun getRoutWithArgs(connection: Connection): String {
            val connectionJson = Gson().toJson(connection)
            return "$ROUT_FOR_ARGS/${connectionJson.encode()}"
        }
    }

    object Dashboard : Screen(ROUTE_DASHBOARD)
    object Settings : Screen(ROUTE_SETTINGS)

    companion object {

        const val KEY_CONNECTION = "connection"

        const val ROUTE_CONNECTIONS_CONTAINER = "connections_container"
        const val ROUTE_CONNECTIONS = "connections"
        const val ROUTE_TRADES = "trades/{$KEY_CONNECTION}"
        const val ROUTE_DASHBOARD = "dashboard"
        const val ROUTE_SETTINGS = "settings"
    }
}

fun String.encode(): String {
    return Uri.encode(this)
}