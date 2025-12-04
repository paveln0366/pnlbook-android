package com.pnlbook.presentation.trades

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pnlbook.R
import com.pnlbook.domain.Connection
import com.pnlbook.domain.Trade

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradesScreen(
    connection: Connection,
    onBackPressed: () -> Unit
) {
    val viewModel: TradesViewModel = viewModel(
        factory = TradesViewModelFactory(
            connection,
            LocalContext.current.applicationContext as Application
        )
    )
    val screenState = viewModel.screenState.observeAsState(TradesScreenState.Initial)
    val currentState = screenState.value

    if (currentState is TradesScreenState.Trades) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(R.string.trades) + " ${currentState.connection.id}")
                    },
                    navigationIcon = {
                        IconButton(onClick = { onBackPressed() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(
                    top = 16.dp,
                    bottom = 72.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = currentState.trades,
                    key = { it.id }
                ) { trade ->
                    TradeItem(trade = trade)
                }
            }
        }
    }
}

@Composable
private fun TradeItem(
    trade: Trade
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Image(
//            painter = painterResource(id = trade.logo),
            painter = painterResource(id = R.drawable.ic_bitcoin),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = trade.name)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = trade.type.name)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}