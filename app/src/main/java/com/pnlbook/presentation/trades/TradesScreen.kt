package com.pnlbook.presentation.trades

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.pnlbook.domain.Connection

@Composable
fun TradesScreen(
    onBackPressed: () -> Unit,
    connection: Connection
) {
    Text(text = "TradesScreen")
}