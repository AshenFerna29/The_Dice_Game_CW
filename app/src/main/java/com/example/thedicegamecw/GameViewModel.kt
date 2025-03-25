package com.example.thedicegamecw

import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    var humanWins = 0
    var computerWins = 0

    fun resetWins() {
        humanWins = 0
        computerWins = 0
    }
}
