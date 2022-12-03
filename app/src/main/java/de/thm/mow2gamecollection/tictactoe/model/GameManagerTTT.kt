package de.thm.mow2gamecollection.tictactoe.model

import android.os.CountDownTimer
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.tictactoe.controller.FieldSize
import de.thm.mow2gamecollection.tictactoe.controller.TicTacToeActivity

class GameManagerTTT (val controller: TicTacToeActivity){
    //x is starting
    private var currentPlayer = 1
    var player1Points = 0
    var player2Points  = 0

    val currentPlayerMark : String
        get() {
            return if (currentPlayer == 1) "x" else "o"
        }

    lateinit var fieldSize : FieldSize

    private var state3x3 = arrayOf(
                intArrayOf(0,0,0),
                intArrayOf(0,0,0),
                intArrayOf(0,0,0)
            )

    private var state5x5 = arrayOf(
                intArrayOf(0,0,0,0,0),
                intArrayOf(0,0,0,0,0),
                intArrayOf(0,0,0,0,0),
                intArrayOf(0,0,0,0,0),
                intArrayOf(0,0,0,0,0))

    
    fun makeMove (position: Position) : WinningLine? {
        //fun makeMove (position: Position) : Boolean {
        state3x3[position.row][position.column] = currentPlayer
        val winningLine = hasGameEnded()
        if (winningLine == null) {
            currentPlayer = 3 - currentPlayer
        }else{
            when(currentPlayer){
                1 -> player1Points++
                2 -> player2Points++
            }
        }
        return winningLine
    }

    fun makeMove5x5 (position: Position) : WinningLine5x5? {
        //fun makeMove (position: Position) : Boolean {
        state5x5[position.row][position.column] = currentPlayer
        val winningLine = hasGameEnded5x5()
        if (winningLine == null) {
            currentPlayer = 3 - currentPlayer
        }else{
            when(currentPlayer){
                1 -> player1Points++
                2 -> player2Points++
            }
        }
        return winningLine
    }

    fun reset() {
        state3x3 = arrayOf(
            intArrayOf(0,0,0),
            intArrayOf(0,0,0),
            intArrayOf(0,0,0)
        )
        currentPlayer = 1
    }

    fun reset5x5() {
        state5x5 = arrayOf(
            intArrayOf(0,0,0,0,0),
            intArrayOf(0,0,0,0,0),
            intArrayOf(0,0,0,0,0),
            intArrayOf(0,0,0,0,0),
            intArrayOf(0,0,0,0,0))
        currentPlayer = 1
    }

    private fun hasGameEnded(): WinningLine? {
        if (state3x3[0][0] == currentPlayer && state3x3[0][1] == currentPlayer && state3x3[0][2] == currentPlayer) {
            return WinningLine.ROW_0
        } else if (state3x3[1][0] == currentPlayer && state3x3[1][1] == currentPlayer && state3x3[1][2] == currentPlayer) {
            return WinningLine.ROW_1
        } else if (state3x3[2][0] == currentPlayer && state3x3[2][1] == currentPlayer && state3x3[2][2] == currentPlayer) {
            return WinningLine.ROW_2
        } else if (state3x3[0][0] == currentPlayer && state3x3[1][0] == currentPlayer && state3x3[2][0] == currentPlayer) {
            return WinningLine.COLUMN_0
        } else if (state3x3[0][1] == currentPlayer && state3x3[1][1] == currentPlayer && state3x3[2][1] == currentPlayer) {
            return WinningLine.COLUMN_1
        } else if (state3x3[0][2] == currentPlayer && state3x3[1][2] == currentPlayer && state3x3[2][2] == currentPlayer) {
            return WinningLine.COLUMN_2
        } else if (state3x3[0][0] == currentPlayer && state3x3[1][1] == currentPlayer && state3x3[2][2] == currentPlayer) {
            return WinningLine.DIAGONAL_LEFT
        } else if (state3x3[0][2] == currentPlayer && state3x3[1][1] == currentPlayer && state3x3[2][0] == currentPlayer) {
            return WinningLine.DIAGONAL_RIGHT
        }
        return null

    }

    private fun hasGameEnded5x5(): WinningLine5x5? {
        if (state5x5[0][0] == currentPlayer && state5x5[0][1] == currentPlayer && state5x5[0][2] == currentPlayer) {
            return WinningLine5x5.ROW_0
        } else if (state5x5[1][0] == currentPlayer && state5x5[1][1] == currentPlayer && state5x5[1][2] == currentPlayer) {
            return WinningLine5x5.ROW_1
        } else if (state5x5[2][0] == currentPlayer && state5x5[2][1] == currentPlayer && state5x5[2][2] == currentPlayer) {
            return WinningLine5x5.ROW_2
        } else if (state5x5[0][0] == currentPlayer && state5x5[1][0] == currentPlayer && state5x5[2][0] == currentPlayer) {
            return WinningLine5x5.COLUMN_0
        } else if (state5x5[0][1] == currentPlayer && state5x5[1][1] == currentPlayer && state5x5[2][1] == currentPlayer) {
            return WinningLine5x5.COLUMN_1
        } else if (state5x5[0][2] == currentPlayer && state5x5[1][2] == currentPlayer && state5x5[2][2] == currentPlayer) {
            return WinningLine5x5.COLUMN_2
        } else if (state5x5[0][0] == currentPlayer && state5x5[1][1] == currentPlayer && state5x5[2][2] == currentPlayer) {
            return WinningLine5x5.DIAGONAL_LEFT
        } else if (state5x5[0][2] == currentPlayer && state5x5[1][1] == currentPlayer && state5x5[2][0] == currentPlayer) {
            return WinningLine5x5.DIAGONAL_RIGHT
        }
        return null

    }

}




