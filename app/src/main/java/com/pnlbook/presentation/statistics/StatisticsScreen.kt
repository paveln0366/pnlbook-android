package com.pnlbook.presentation.statistics

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pnlbook.domain.Connection

@Composable
fun StatisticsScreen(
    onBackPressed: () -> Unit,
    connection: Connection
) {
    val viewModel: StatisticsViewModel = viewModel()

    Text(text = "StatisticsScreen")
}