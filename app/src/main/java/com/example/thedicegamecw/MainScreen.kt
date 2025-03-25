package com.example.dicegamecw
import androidx.compose.ui.platform.LocalConfiguration
import android.content.res.Configuration

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.thedicegamecw.R

@Composable
fun MainScreen(navController: NavController) {
    var showAbout by remember { mutableStateOf(false) }

    // Detect screen width to toggle layout
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0D47A1), Color(0xFF42A5F5))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isLandscape) {
            // 📱 Landscape Mode
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Left: Dice Image
                Image(
                    painter = painterResource(id = R.drawable.dice),
                    contentDescription = "Welcome Image",
                    modifier = Modifier
                        .height(350.dp)
                        .width(350.dp),
                    contentScale = ContentScale.Fit
                )

                // Right: Text & Buttons
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Welcome to Dice Dash!",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { navController.navigate("gameScreen") },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFADD7FE)),
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Text("New Game", fontSize = 18.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { showAbout = true },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFADD7FE)),
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Text("About", fontSize = 18.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            // 📲 Portrait Mode (your original layout)
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.dice),
                    contentDescription = "Welcome Image",
                    modifier = Modifier
                        .height(450.dp)
                        .width(450.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Welcome to Dice Dash!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = { navController.navigate("gameScreen") },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFADD7FE)),
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text("New Game", fontSize = 18.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { showAbout = true },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFADD7FE)),
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text("About", fontSize = 18.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // About Dialog
    if (showAbout) {
        AlertDialog(
            onDismissRequest = { showAbout = false },
            title = { Text("About", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
            text = {
                Text(
                    "Author: Ashen Fernando \nStudent ID: 2023324\n\n" +
                            "I confirm that I understand what plagiarism is and have read and " +
                            "understood the section on Assessment Offences in the Essential " +
                            "Information for Students. The work that I have submitted is " +
                            "entirely my own.",
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                Button(onClick = { showAbout = false }) {
                    Text("OK", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
