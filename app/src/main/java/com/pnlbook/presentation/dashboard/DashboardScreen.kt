package com.pnlbook.presentation.dashboard

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pnlbook.R
import com.pnlbook.presentation.chart.BtcChartScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {
    val viewModel: DashboardViewModel = viewModel()

    Scaffold(
        modifier = Modifier.padding(bottom = 72.dp),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.dashboard))
                }
            )
        }
    ) { paddingValues ->
        BtcChartScreen(paddingValues)
    }
}