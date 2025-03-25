package com.example.thedicegamecw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.*
import androidx.compose.material.*
import com.example.dicegamecw.MainScreen
import com.example.thedicegamecw.GameScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            NavHost(navController, startDestination = "mainScreen") {
                composable("mainScreen") { MainScreen(navController) }
                composable("gameScreen") { GameScreen(navController) }
            }
        }
    }
}
