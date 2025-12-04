package com.pnlbook.presentation.trades

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pnlbook.domain.Connection

class TradesViewModelFactory(
    private val connection: Connection,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TradesViewModel(connection, application) as T
    }
}