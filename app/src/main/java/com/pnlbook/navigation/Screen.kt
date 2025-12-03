package com.pnlbook.navigation

sealed class Screen(
    val route: String
) {
    object Connections : Screen(ROUTE_CONNECTIONS)
    object Dashboard : Screen(ROUTE_DASHBOARD)
    object Settings : Screen(ROUTE_SETTINGS)

    companion object {

        const val ROUTE_CONNECTIONS = "connections"
        const val ROUTE_DASHBOARD = "dashboard"
        const val ROUTE_SETTINGS = "settings"
    }
}