package com.example.upstats

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface(color = MaterialTheme.colorScheme.background) {
                DisplayForegroundTime()
            }
        }
    }

}

@Composable
fun DisplayForegroundTime() {
    val viewModel: MainViewModel = viewModel()
    val foregroundTime by viewModel.totalTimeForeground.observeAsState()
    val backgroundTime by viewModel.totalTimeBackground.observeAsState()
    val currentforegroundTime by viewModel.currentTimeForeground.observeAsState()
    val currentbackgroundTime by viewModel.currentTimeBackground.observeAsState()

    Surface {
        Column {
            Text("Total foreground time: $foregroundTime ms")
            Text("Total background time: $backgroundTime ms")

            Text("Current Session foreground time: $currentforegroundTime ms")
            Text("Current Session background time: $currentbackgroundTime ms")
        }
    }
}
