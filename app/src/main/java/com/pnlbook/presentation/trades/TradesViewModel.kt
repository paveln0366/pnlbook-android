package com.pnlbook.presentation.trades

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pnlbook.domain.Connection
import com.pnlbook.domain.Trade

class TradesViewModel(
    connection: Connection,
    application: Application,
) : AndroidViewModel(application) {

    private val _screenState = MutableLiveData<TradesScreenState>(TradesScreenState.Initial)
    val screenState: LiveData<TradesScreenState> = _screenState

    init {
        loadTrades(connection)
    }

    private fun loadTrades(connection: Connection) {
        val trades = mutableListOf<Trade>().apply {
            repeat(10) {
                add(Trade(id = it))
            }
        }
        _screenState.value = TradesScreenState.Trades(
            connection = connection,
            trades = trades
        )
    }
}