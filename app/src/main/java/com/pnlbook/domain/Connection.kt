package com.pnlbook.domain

import android.os.Parcelable
import com.pnlbook.R
import kotlinx.parcelize.Parcelize

enum class Exchange {
    BINANCE, BYBIT, OKX, MEXC, BINGX, GATE, KUCOIN, OURBIT
}

enum class ConnectionType {
    SPOT, FEATURES
}

@Parcelize
class Connection(
    val id: Int = 0,
    val exchange: Exchange = Exchange.BINANCE,
    val type: ConnectionType = ConnectionType.FEATURES,
    val name: String = "Binance futures",
    val logo: Int = R.drawable.logo_binance
) : Parcelable