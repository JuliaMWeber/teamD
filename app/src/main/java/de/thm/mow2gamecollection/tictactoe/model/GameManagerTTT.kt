package de.thm.mow2gamecollection.tictactoe.model

import de.thm.mow2gamecollection.tictactoe.controller.TicTacToeActivity

class GameManagerTicTacToe (val controller: TicTacToeActivity){

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

    /**
     * Sets chosen position for player and checks if win or draw happened
     * @param position[row,column] Position in grid
     * @return null if no win otherwise a WinningLine or WinningLine.NOWINNER for a draw
     */
    fun makeMove (position: Position) : WinningLine? {
        state[position.row][position.column] = currentPlayer
        val winningLine = hasGameEnded()
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
        state = arrayOf(
            intArrayOf(0,0,0),
            intArrayOf(0,0,0),
            intArrayOf(0,0,0)
        )
        currentPlayer = 1
    }

    /**
     * Checks if game ended
     * @return a winningline if win or draw happens
     */
    private fun hasGameEnded(): WinningLine? {
        if (
            state[0][0] != 0 && state[1][0] != 0  && state[2][0] != 0  &&
            state[0][1] != 0 && state[1][1] != 0  && state[2][1] != 0  &&
            state[0][2] != 0 && state[1][2] != 0  && state[2][2] != 0
        ) return WinningLine.NOWINNER

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

}





