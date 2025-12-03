package com.pnlbook.domain

import android.os.Parcelable
import androidx.navigation.NavType
import androidx.savedstate.SavedState
import com.google.gson.Gson
import com.pnlbook.R
import kotlinx.parcelize.Parcelize

enum class ConnectionType {
    BINANCE, BYBIT, OKX
}

@Parcelize
class Connection(
    val id: Int = 0,
    val type: ConnectionType = ConnectionType.BINANCE,
    val name: String = "Binance futures",
    val logo: Int = R.drawable.logo_binance
) : Parcelable {

    companion object {

        val NavigationType: NavType<Connection> = object : NavType<Connection>(false) {
            override fun get(bundle: SavedState, key: String): Connection? {
                return bundle.getParcelable(key)
            }

            override fun parseValue(value: String): Connection {
                return Gson().fromJson(value, Connection::class.java)
            }

            override fun put(bundle: SavedState, key: String, value: Connection) {
                bundle.putParcelable(key, value)
            }
        }
    }
}