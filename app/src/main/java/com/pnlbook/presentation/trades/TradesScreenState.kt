package com.pnlbook.presentation.trades

import com.pnlbook.domain.Connection
import com.pnlbook.domain.Trade

sealed class TradesScreenState {

    object Initial : TradesScreenState()

    data class Trades(
        val connection: Connection,
        val trades: List<Trade>
    ) : TradesScreenState()
}