package com.pnlbook.presentation.connections

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pnlbook.domain.Connection

@Composable
fun ConnectionsScreen(
    paddingValues: PaddingValues,
    onTradesClickListener: (Connection) -> Unit,
    onStatisticClickListener: (Connection) -> Unit
) {
    Box(
        modifier = Modifier.padding(paddingValues),
    ) {
        Text(text = "ConnectionsScreen")
    }
}