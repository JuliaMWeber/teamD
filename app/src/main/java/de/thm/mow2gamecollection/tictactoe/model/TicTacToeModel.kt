package de.thm.mow2gamecollection.tictactoe.model

import de.thm.mow2gamecollection.tictactoe.controller.TicTacToeActivity
import de.thm.mow2gamecollection.tictactoe.model.game.*

interface TicTacToeController {
    fun updatePoints()
    fun showActivePlayer()
    fun getGameMode(): GameMode
    fun getFieldsize(): FieldSize
    fun restartTimer()
}

class TicTacToeModel (val controller: TicTacToeActivity){

    private var currentPlayer = 1
    var player1Points = 0
    var player2Points  = 0

    val currentPlayerMark : String
        get() {
            return if (currentPlayer == 1) "x" else "o"
        }

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
        intArrayOf(0,0,0,0,0)
    )

    /**
     * Sets chosen position for player and checks if win or draw happened
     * @param position[row,column] Position in grid
     * @return null if no win otherwise a WinningLine or WinningLine.NOWINNER for a draw
     */
    fun makeMove3x3 (position: Position) : WinningLine3x3? {
        state3x3[position.row][position.column] = currentPlayer
        val winningLine = hasGameEnded3x3()
        if (winningLine == null) {
            changeActivePlayer()
        }else{
            if (currentPlayer == 1) player1Points++ else player2Points++
            controller.updatePoints()
        }
        return winningLine
    }

    fun makeMove5x5 (position: Position) : WinningLine5x5? {
        state5x5[position.row][position.column] = currentPlayer
        val winningLine = hasGameEnded5x5()
        if (winningLine == null) {
            changeActivePlayer()
        }else{
            if (currentPlayer == 1) player1Points++ else player2Points++
            controller.updatePoints()
        }
        return winningLine
    }

    /**
     * Changes the active Player and restarts timer on hardmode
     */
    fun changeActivePlayer() {
        currentPlayer = if (currentPlayer==1) 2 else 1
        controller.showActivePlayer()
        if (controller.getGameMode() === GameMode.HARD){
            controller.restartTimer()
        }
    }

    /**
     * Resets the grid
     */
    fun resetGrid() {
        if (controller.getFieldsize()== FieldSize.THREE) {
            state3x3 = arrayOf(
                intArrayOf(0,0,0),
                intArrayOf(0,0,0),
                intArrayOf(0,0,0)
            )
        } else {
            state5x5 = arrayOf(
                intArrayOf(0,0,0,0,0),
                intArrayOf(0,0,0,0,0),
                intArrayOf(0,0,0,0,0),
                intArrayOf(0,0,0,0,0),
                intArrayOf(0,0,0,0,0)
            )
        }
        currentPlayer = 1
    }

    /**
     * Checks if game ended
     * @return a winningline if win or draw happens
     */
    private fun hasGameEnded3x3(): WinningLine3x3? {
        if (
            state3x3[0][0] != 0 && state3x3[1][0] != 0  && state3x3[2][0] != 0  &&
            state3x3[0][1] != 0 && state3x3[1][1] != 0  && state3x3[2][1] != 0  &&
            state3x3[0][2] != 0 && state3x3[1][2] != 0  && state3x3[2][2] != 0
        ) return WinningLine3x3.NOWINNER

        if (state3x3[0][0] == currentPlayer && state3x3[0][1] == currentPlayer && state3x3[0][2] == currentPlayer) {
            return WinningLine3x3.ROW_0
        } else if (state3x3[1][0] == currentPlayer && state3x3[1][1] == currentPlayer && state3x3[1][2] == currentPlayer) {
            return WinningLine3x3.ROW_1
        } else if (state3x3[2][0] == currentPlayer && state3x3[2][1] == currentPlayer && state3x3[2][2] == currentPlayer) {
            return WinningLine3x3.ROW_2
        } else if (state3x3[0][0] == currentPlayer && state3x3[1][0] == currentPlayer && state3x3[2][0] == currentPlayer) {
            return WinningLine3x3.COLUMN_0
        } else if (state3x3[0][1] == currentPlayer && state3x3[1][1] == currentPlayer && state3x3[2][1] == currentPlayer) {
            return WinningLine3x3.COLUMN_1
        } else if (state3x3[0][2] == currentPlayer && state3x3[1][2] == currentPlayer && state3x3[2][2] == currentPlayer) {
            return WinningLine3x3.COLUMN_2
        } else if (state3x3[0][0] == currentPlayer && state3x3[1][1] == currentPlayer && state3x3[2][2] == currentPlayer) {
            return WinningLine3x3.DIAGONAL_LEFT
        } else if (state3x3[0][2] == currentPlayer && state3x3[1][1] == currentPlayer && state3x3[2][0] == currentPlayer) {
            return WinningLine3x3.DIAGONAL_RIGHT
        }
        return null
    }

    private fun hasGameEnded5x5(): WinningLine5x5? {
        if (state5x5[0][0] == currentPlayer && state5x5[0][1] == currentPlayer && state5x5[0][2] == currentPlayer && state5x5[0][3] == currentPlayer && state5x5[0][4] == currentPlayer) {
            return WinningLine5x5.ROW_0
        } else if (state5x5[1][0] == currentPlayer && state5x5[1][1] == currentPlayer && state5x5[1][2] == currentPlayer && state5x5[1][3] == currentPlayer &&state5x5[1][4] == currentPlayer) {
            return WinningLine5x5.ROW_1
        } else if (state5x5[2][0] == currentPlayer && state5x5[2][1] == currentPlayer && state5x5[2][2] == currentPlayer) {
            return WinningLine5x5.ROW_2
        } else if (state5x5[0][0] == currentPlayer && state5x5[1][0] == currentPlayer && state5x5[2][0] == currentPlayer && state5x5[3][0] == currentPlayer &&state5x5[4][0] == currentPlayer) {
            return WinningLine5x5.COLUMN_0
        } else if (state5x5[0][1] == currentPlayer && state5x5[1][1] == currentPlayer && state5x5[2][1] == currentPlayer&& state5x5[3][1] == currentPlayer && state5x5[4][1] == currentPlayer) {
            return WinningLine5x5.COLUMN_1
        } else if (state5x5[0][2] == currentPlayer && state5x5[1][2] == currentPlayer && state5x5[2][2] == currentPlayer && state5x5[3][2] == currentPlayer && state5x5[4][2] == currentPlayer) {
            return WinningLine5x5.COLUMN_2
        } else if (state5x5[0][0] == currentPlayer && state5x5[1][1] == currentPlayer && state5x5[2][2] == currentPlayer && state5x5[3][3] == currentPlayer && state5x5[4][4] == currentPlayer) {
            return WinningLine5x5.DIAGONAL_LEFT
        } else if (state5x5[4][0] == currentPlayer && state5x5[3][1] == currentPlayer && state5x5[2][2] == currentPlayer && state5x5[1][3] == currentPlayer && state5x5[0][4] == currentPlayer) {
            return WinningLine5x5.DIAGONAL_RIGHT
        }
        return null
    }

}




