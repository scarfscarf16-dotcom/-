package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.VideoGeneratorScreen
import com.example.ui.theme.IslamicQuranStudioTheme
import com.example.ui.viewmodel.QuranStudioViewModel

sealed class AppScreen {
    object Home : AppScreen()
    object VideoStudio : AppScreen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            IslamicQuranStudioTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    QuranAppNavigation()
                }
            }
        }
    }
}

@Composable
fun QuranAppNavigation(
    viewModel: QuranStudioViewModel = viewModel()
) {
    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Home) }

    when (currentScreen) {
        is AppScreen.Home -> {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToStudio = { currentScreen = AppScreen.VideoStudio },
                onNavigateToSaved = { currentScreen = AppScreen.VideoStudio }
            )
        }
        is AppScreen.VideoStudio -> {
            androidx.activity.compose.BackHandler {
                currentScreen = AppScreen.Home
            }
            VideoGeneratorScreen(
                viewModel = viewModel,
                onNavigateBack = { currentScreen = AppScreen.Home }
            )
        }
    }
}
