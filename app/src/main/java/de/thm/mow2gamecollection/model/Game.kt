package de.thm.mow2gamecollection.model

data class Game(val name: String, val imageId: Int, val isNetworkMultiplayer: Boolean) {
    companion object {
        const val SUDOKU_NAME = "Sudoku"
        const val WORDLE_NAME = "Wordle"
        const val TICTACTOE_NAME = "Tic Tac Toe (Hotseat)"
        const val TICTACTOE_NETWORK_NAME = "Tic Tac Toe (Netzwerk-Multiplayer)"
    }
}

