package com.pnlbook.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ConnectionItem(
    val id: Int,
    val type: ConnectionType
) : Parcelable

enum class ConnectionType {
    BINANCE, OKX, BYBIT
}