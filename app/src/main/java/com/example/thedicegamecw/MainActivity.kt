package com.example.thedicegamecw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.*
import com.example.thedicegamecw.ui.theme.TheDiceGameCWTheme
import com.example.dicegamecw.MainScreen

class MainActivity : ComponentActivity() {

    private val gameViewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            TheDiceGameCWTheme {
                NavHost(navController, startDestination = "mainScreen") {
                    composable("mainScreen") {
                        MainScreen(navController)
                    }
                    composable("gameScreen") {
                        GameScreen(navController, gameViewModel)
                    }
                }
            }
        }
    }
}
