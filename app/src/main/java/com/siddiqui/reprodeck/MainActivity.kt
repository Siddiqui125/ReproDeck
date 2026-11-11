package com.siddiqui.reprodeck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.siddiqui.reprodeck.ui.ReproDeckApp
import com.siddiqui.reprodeck.ui.theme.ReproDeckTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = (application as ReproDeckApplication).snapshotRepository
        setContent {
            ReproDeckTheme {
                ReproDeckApp(repository = repository)
            }
        }
    }
}
