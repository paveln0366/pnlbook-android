package com.pnlbook.presentation.connections

import com.pnlbook.domain.Connection

sealed class ConnectionsScreenState {

    object Initial : ConnectionsScreenState()

    data class Connections(val connections: List<Connection>) : ConnectionsScreenState()
}