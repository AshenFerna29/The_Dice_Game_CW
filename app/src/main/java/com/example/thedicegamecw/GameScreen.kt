package com.example.thedicegamecw

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun GameScreen(navController: NavController) {
    var humanDice by remember { mutableStateOf(List(5) { Random.nextInt(1, 7) }) }
    var computerDice by remember { mutableStateOf(List(5) { Random.nextInt(1, 7) }) }
    var humanScore by remember { mutableStateOf(0) }
    var computerScore by remember { mutableStateOf(0) }
    var isRolling by remember { mutableStateOf(false) }
    var targetScore by remember { mutableStateOf(101) }
    var rollCount by remember { mutableStateOf(0) }
    var gameStarted by remember { mutableStateOf(false) }
    var winner by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedDice by remember { mutableStateOf(mutableSetOf<Int>()) } // Selected dice for reroll
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    if (!gameStarted) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF001F3F), Color(0xFF0074D9))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Set Target Score", fontSize = 24.sp, color = Color.White)

                var textValue by remember { mutableStateOf(targetScore.toString()) }

                TextField(
                    value = textValue,
                    onValueChange = { newValue ->
                        textValue = newValue
                        targetScore = newValue.toIntOrNull() ?: 101
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .shadow(5.dp, RoundedCornerShape(8.dp)),
                    placeholder = { Text("Enter Target Score", color = Color.White) }
                )

                Button(
                    onClick = { gameStarted = true },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF00C853)),
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text("Start Game", fontSize = 20.sp, color = Color.White)
                }
            }
        }
    } else {
        Scaffold(
            scaffoldState = scaffoldState
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF001F3F), Color(0xFF0074D9))
                        )
                    )
                    .padding(contentPadding)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🎲 Dice Clash 🎲", fontSize = 32.sp, color = Color.White)
                        Text("🎯 Target Score: $targetScore", fontSize = 20.sp, color = Color.Yellow)
                    }

                    Text(
                        text = "Marks 🎯\nYou: $humanScore | CPU: $computerScore",
                        fontSize = 14.sp,
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(end = 8.dp)
                    )
                }


                Spacer(modifier = Modifier.height(30.dp))

                Text("👤 Your Dice (Click to Keep)", color = Color.White, fontSize = 22.sp)
                SelectableDiceRow(humanDice, selectedDice)

                Spacer(modifier = Modifier.height(20.dp))

                Text("🤖 Computer's Dice", color = Color.White, fontSize = 22.sp)
                DiceRow(computerDice,isRolling)

                Spacer(modifier = Modifier.height(30.dp))

                if (winner == null) {
                    Row {
                        Button(
                            onClick = {
                                isRolling = true
                                coroutineScope.launch {
                                    animateSelectiveDiceRoll(humanDice, selectedDice) { newHumanDice ->
                                        humanDice = newHumanDice
                                        computerDice = List(5) { Random.nextInt(1, 7) }
                                    }
                                    isRolling = false
                                    rollCount++

                                    if (rollCount >= 3) {
                                        // 👇 Auto-add score after 3 throws
                                        humanScore += humanDice.sum()
                                        computerScore += computerDice.sum()
                                        rollCount = 0
                                        selectedDice.clear()

                                        // ✅ Show snackbar
                                        scaffoldState.snackbarHostState.showSnackbar("✅ Score added automatically after 3 rolls!")

                                        // 🎉 Check for winner
                                        checkWinner(humanScore, computerScore, targetScore) { result ->
                                            winner = result
                                        }
                                    }
                                }
                            },

                                    shape = RoundedCornerShape(15.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF00C853)),
                            modifier = Modifier
                                .padding(10.dp)
                                .shadow(8.dp, RoundedCornerShape(15.dp)),
                            enabled = winner == null
                        ) {
                            Text("🎲 Throw", fontSize = 20.sp, color = Color.White)
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Button(
                            onClick = {
                                humanScore += humanDice.sum()
                                computerScore += computerDice.sum()
                                rollCount = 0
                                selectedDice.clear()
                                checkWinner(humanScore, computerScore, targetScore) { result ->
                                    winner = result
                                }
                            },
                            shape = RoundedCornerShape(15.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFC107)),
                            modifier = Modifier
                                .padding(10.dp)
                                .shadow(8.dp, RoundedCornerShape(15.dp)),
                            enabled = winner == null
                        ) {
                            Text("✅ Score", fontSize = 20.sp, color = Color.Black)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text("🔄 Roll Count: ${rollCount}/3", color = Color.White, fontSize = 18.sp)

                    Spacer(modifier = Modifier.height(10.dp))
                }

                Text(
                    "🏆 Score: You - $humanScore | Computer - $computerScore",
                    color = Color.White,
                    fontSize = 22.sp
                )

                LaunchedEffect(winner) {
                    if (winner != null) {
                        showDialog = true
                    }
                }

                if (showDialog && winner != null) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = {
                            Text(text = winner!!, fontSize = 24.sp)
                        },
                        text = {
                            Text(
                                when {
                                    winner!!.contains("Win") -> "Congratulations! You won the Dice Dash."
                                    winner!!.contains("Lose") -> "Oops! You lost . Better luck next time."
                                    else -> "It's a perfect tie. Lets go again"
                                },
                                fontSize = 18.sp
                            )
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showDialog = false
                                }
                            ) {
                                Text("OK")
                            }
                        },
                        backgroundColor = Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
                }


                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { navController.popBackStack() },
                    shape = RoundedCornerShape(15.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFD50000)),
                    modifier = Modifier
                        .padding(10.dp)
                        .shadow(8.dp, RoundedCornerShape(15.dp))
                ) {
                    Text("🏠 Back to Main Menu", fontSize = 20.sp, color = Color.White)
                }
            }
        }
    }
}

// ✅ Selectable Dice Row
@Composable
fun SelectableDiceRow(dice: List<Int>, selectedDice: MutableSet<Int>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        dice.forEachIndexed { index, value ->
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .padding(4.dp)
                    .background(
                        color = if (selectedDice.contains(index)) Color(0xFFB2FF59) else Color.Transparent, // Green highlight
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable {
                        selectedDice.clear()
                        selectedDice.add(index) // Only one die can be selected at a time
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = getDiceImage(value)),
                    contentDescription = "Dice Image",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}



// ✅ Dice Rolling with Selection
suspend fun animateSelectiveDiceRoll(
    dice: List<Int>,
    selectedDice: MutableSet<Int>,
    onRollComplete: (List<Int>) -> Unit
) {
    val newDice = dice.mapIndexed { index, value ->
        if (selectedDice.contains(index)) value else Random.nextInt(1, 7)
    }
    delay(100)
    onRollComplete(newDice)
}


// ✅ Function to Check Winner
fun checkWinner(humanScore: Int, computerScore: Int, targetScore: Int, onWin: (String?) -> Unit) {
    when {
        humanScore >= targetScore && computerScore >= targetScore -> {
            onWin(
                if (humanScore > computerScore) "You Win! 🎉"
                else if (computerScore > humanScore) "You Lose 😞"
                else "It's a Tie! 🔄"
            )
        }
        humanScore >= targetScore -> onWin("You Win! 🎉")
        computerScore >= targetScore -> onWin("You Lose 😞")
    }
}


// 🚀 Dice Rolling Animation
suspend fun animateDiceRoll(onRollComplete: (List<Int>, List<Int>) -> Unit) {
    for (i in 1..5) {
        val newHumanDice = List(5) { Random.nextInt(1, 7) }
        val newComputerDice = List(5) { Random.nextInt(1, 7) }
        onRollComplete(newHumanDice, newComputerDice)
        delay(100)
    }
}

fun getDiceImage(value: Int): Int {
    return when (value) {
        1 -> R.drawable.dice1
        2 -> R.drawable.dice2
        3 -> R.drawable.dice3
        4 -> R.drawable.dice4
        5 -> R.drawable.dice5
        else -> R.drawable.dice6
    }
}

// 🎲 Dice Row UI
@Composable
fun DiceRow(dice: List<Int>, isRolling: Boolean) {
    Row(horizontalArrangement = Arrangement.Center) {
        dice.forEach {
            Image(
                painter = painterResource(id = getDiceImage(it)),
                contentDescription = "Dice Image",
                modifier = Modifier
                    .size(80.dp)
                    .padding(4.dp)
            )
        }
    }
}
