package com.pnlbook.domain

import android.os.Parcelable
import com.pnlbook.R
import kotlinx.parcelize.Parcelize

enum class TradeType {
    LONG, SHORT
}

@Parcelize
class Trade(
    val id: Int = 0,
    val logo: Int = R.drawable.logo_binance,
    val name: String = "Binance futures",
    val type: TradeType = TradeType.LONG,
    val pnl: Float = 100f,
    val percent: Float = 10f,
    val volume: Float = 1000f
) : Parcelable {
}