package com.pnlbook.presentation.connections

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pnlbook.domain.Connection
import com.pnlbook.domain.ConnectionItem

@Composable
fun ConnectionCard(
    modifier: Modifier = Modifier,
    connection: Connection,
    onTradesClickListener: (ConnectionItem) -> Unit,
    onStatisticsClickListener: (ConnectionItem) -> Unit
) {
}