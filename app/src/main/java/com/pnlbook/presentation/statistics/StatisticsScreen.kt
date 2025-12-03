package com.pnlbook.presentation.statistics

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.pnlbook.domain.Connection

@Composable
fun StatisticsScreen(
    onBackPressed: () -> Unit,
    connection: Connection
) {
    Text(text = "StatisticsScreen")
}