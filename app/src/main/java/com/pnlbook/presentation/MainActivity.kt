package com.pnlbook.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pnlbook.presentation.main.MainScreen
import com.pnlbook.ui.theme.PnlbookTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PnlbookTheme {
                MainScreen()
            }
        }
    }
}