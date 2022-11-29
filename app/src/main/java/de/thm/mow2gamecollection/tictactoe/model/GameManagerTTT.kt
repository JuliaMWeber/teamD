package de.thm.mow2gamecollection.tictactoe.model

import android.util.Log
import de.thm.mow2gamecollection.model.EmulatorEnabledMultiplayerGame
import de.thm.mow2gamecollection.service.EmulatorNetworkingService
import de.thm.mow2gamecollection.tictactoe.controller.TicTacToeActivity

// DEBUGGING
private const val DEBUG = true
private const val TAG = "GameManagerTTT"

class GameManagerTTT (val controller: TicTacToeActivity, val gameMode: GameMode): EmulatorEnabledMultiplayerGame {

    override var emulatorNetworkingService: EmulatorNetworkingService? = null

    //x is starting
    private var currentPlayer = 1
    var player1Points = 0
    var player2Points  = 0

    val currentPlayerMark : String
        get() {
            return if (currentPlayer == 1) "x" else "o"
        }

    private var state = arrayOf(
        intArrayOf(0,0,0),
        intArrayOf(0,0,0),
        intArrayOf(0,0,0)
    )

    private fun randomFreePosition() : Position {
        Log.d(TAG, "randomFreePosition")
        fun randomPosition() : Position {
            val row = (0..2).random()
            val column = (0..2).random()
            return Position(row, column)
        }

        var position: Position
        do {
            position = randomPosition()
        } while (state[position.row][position.column] != 0)

        return position
    }

    fun makeMove (position: Position) : WinningLine? {
        Log.d(TAG, "makeMove ${position.toString()}")

        if (gameMode == GameMode.NETWORK_MULTIPLAYER) {
            // send move to opponent
            sendNetworkMessage("${position.row};${position.column}")
        }

        //fun makeMove (position: Position) : Boolean {
        state[position.row][position.column] = currentPlayer
        val winningLine = hasGameEnded()
        if (winningLine == null) {
            currentPlayer = 3 - currentPlayer
            //change game mode
            //fix
            if (controller.gameMode == GameMode.SINGLE && currentPlayer == 2 &&
                (state[0].contains(0) || state[1].contains(0) || state[2].contains(0))) {
                val pcPosition = randomFreePosition()
                controller.onFieldClick(pcPosition)
            }
        } else {
            when (currentPlayer) {
                1 -> player1Points++
                2 -> player2Points++
            }
        }
        return winningLine
    }

    fun reset() {
        state = arrayOf(
            intArrayOf(0,0,0),
            intArrayOf(0,0,0),
            intArrayOf(0,0,0)
        )
        currentPlayer = 1
    }
    private fun hasGameEnded(): WinningLine? {
        if (state[0][0] == currentPlayer && state[0][1] == currentPlayer && state[0][2] == currentPlayer) {
            return WinningLine.ROW_0
        } else if (state[1][0] == currentPlayer && state[1][1] == currentPlayer && state[1][2] == currentPlayer) {
            return WinningLine.ROW_1
        } else if (state[2][0] == currentPlayer && state[2][1] == currentPlayer && state[2][2] == currentPlayer) {
            return WinningLine.ROW_2
        } else if (state[0][0] == currentPlayer && state[1][0] == currentPlayer && state[2][0] == currentPlayer) {
            return WinningLine.COLUMN_0
        } else if (state[0][1] == currentPlayer && state[1][1] == currentPlayer && state[2][1] == currentPlayer) {
            return WinningLine.COLUMN_1
        } else if (state[0][2] == currentPlayer && state[1][2] == currentPlayer && state[2][2] == currentPlayer) {
            return WinningLine.COLUMN_2
        } else if (state[0][0] == currentPlayer && state[1][1] == currentPlayer && state[2][2] == currentPlayer) {
            return WinningLine.DIAGONAL_LEFT
        } else if (state[0][2] == currentPlayer && state[1][1] == currentPlayer && state[2][0] == currentPlayer) {
            return WinningLine.DIAGONAL_RIGHT
        }
        return null
    }

    override fun handleNetworkMessage(msg: String) {
        if (DEBUG) Log.d(TAG, "received message: $msg")
        val msgList = msg.split(";")
        val row = msgList[0].toInt()
        val col = msgList[1].toInt()
        //onFieldClick(getField(row, col), Position(row, col))
        controller.onFieldClick(Position(row,col))
    }
}




