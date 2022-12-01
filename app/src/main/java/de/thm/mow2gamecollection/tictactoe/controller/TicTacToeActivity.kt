package de.thm.mow2gamecollection.tictactoe.controller

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewbinding.ViewBinding
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.databinding.ActivityTicTacToe5x5Binding
import de.thm.mow2gamecollection.databinding.ActivityTicTacToeBinding
import de.thm.mow2gamecollection.model.EmulatorEnabledMultiplayerGame
import de.thm.mow2gamecollection.service.EmulatorNetworkingService
import de.thm.mow2gamecollection.tictactoe.model.GameManagerTTT
import de.thm.mow2gamecollection.tictactoe.model.Position
import de.thm.mow2gamecollection.tictactoe.model.WinningLine

// DEBUGGING
private const val TAG = "TicTacToeActivity"
private const val DEBUG = false // set to true to print debug logs


class TicTacToeActivity : AppCompatActivity(), EmulatorEnabledMultiplayerGame {

    override var emulatorNetworkingService: EmulatorNetworkingService? = null
    private lateinit var binding3x3: ActivityTicTacToeBinding
    private lateinit var binding5x5: ActivityTicTacToe5x5Binding
    private lateinit var gameManagerTTT: GameManagerTTT

    private lateinit var gameMode: GameMode
    private lateinit var fieldSize: FieldSize

    //private var currentPlayer = "x"
    private var playerNumber: Int? = null
    private val allFields3x3 by lazy {
        arrayOf(
            arrayOf(binding3x3.f0, binding3x3.f1, binding3x3.f2,),
            arrayOf(binding3x3.f3, binding3x3.f4, binding3x3.f5),
            arrayOf(binding3x3.f6, binding3x3.f7, binding3x3.f8)
        )
    }

    private val allFields5x5 by lazy {
        arrayOf(
            arrayOf(binding5x5.f27, binding5x5.f0, binding5x5.f1, binding5x5.f2, binding5x5.f9),
            arrayOf(binding5x5.f11, binding5x5.f3, binding5x5.f4, binding5x5.f5, binding5x5.f12),
            arrayOf(binding5x5.f13, binding5x5.f6, binding5x5.f7, binding5x5.f8, binding5x5.f14),
            arrayOf(binding5x5.f16, binding5x5.f18, binding5x5.f19, binding5x5.f25, binding5x5.f21),
            arrayOf(binding5x5.f22, binding5x5.f23, binding5x5.f24, binding5x5.f20, binding5x5.f26)
            )
    }

    // lateinit var gameManager: GameManager
    // private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gameModeString: String? = intent.getStringExtra("gameMode")
        val fieldSizeString: String? = intent.getStringExtra("fieldSize")
        this.gameMode = gameModeString?.let { GameMode.valueOf(it) }!!
        this.fieldSize = fieldSizeString?.let { FieldSize.valueOf(it)}!!

        gameManagerTTT = GameManagerTTT(this)
        if (fieldSize === FieldSize.THREE) {
            binding3x3 = ActivityTicTacToeBinding.inflate(layoutInflater)
            setContentView(binding3x3.root)
            createFieldset(allFields3x3)
            binding3x3.startNewGameButton.setOnClickListener {
                it.visibility = View.GONE
                gameManagerTTT.reset()
                resetFields()
            }
            updatePoints()
        } else {
            binding5x5 = ActivityTicTacToe5x5Binding.inflate(layoutInflater)
            setContentView(binding5x5.root)
            createFieldset(allFields5x5)
            binding5x5.startNewGameButton.setOnClickListener {
                it.visibility = View.GONE
                gameManagerTTT.reset()
                resetFields()
            }
            updatePoints()
        }

        updatePoints()

    }

    private fun createFieldset(allFields: Array<Array<TextView>>) {

        val networkMultiplayerMode = intent.getBooleanExtra("networkMultiplayerMode", false)
        if (DEBUG) Log.d(TAG, "networkMultiplayerMode: $networkMultiplayerMode")
        if(networkMultiplayerMode) {
            playerNumber = intent.getIntExtra("playerNumber", 0)
            if (DEBUG) Log.d(TAG, "playerNumber: $playerNumber")
            initEmulatorNetworkingService(this, intent.getBooleanExtra("isServer", false))
        }

        for (i in allFields.indices) {
            val row = allFields[i]
            for (j in row.indices) {
                val field = row[j]
                field.setOnClickListener {
                    onFieldClick(
                        it as TextView,
                        Position(i, j)
                    )
                }
            }
        }
    }

    private fun ctimer() {
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (fieldSize === FieldSize.THREE) {
                    binding3x3.countdown.text = "übrige Zeit: ${millisUntilFinished / 1000}"
                } else {
                    binding5x5.countdown.text = "übrige Zeit: ${millisUntilFinished / 1000}"
                }
            }

            override fun onFinish() {
                if (fieldSize === FieldSize.THREE) {
                    binding3x3.countdown.text = "Zeit abgelaufen"
                } else{
                    binding5x5.countdown.text = "Zeit abgelaufen"
                }
                resetFields()
            }
        }.start()
    }

/*
    countdown = findViewById(R.id.countdown)
    countdown.text = "timer"
    //val textView = findViewById(R.id.countdown)
    timer =
    object : CountDownTimer(5_000, 100) {

        override fun onTick(remaining: Long) {
            if (DEBUG) Log.d("TicTacToeActivity", "onTick")
            countdown.text = remaining.toString()


        }

        override fun onFinish() {
            countdown.text = "Zeit ist abgelaufen"
        }


    override fun onStart() {
        super.onStart()
        timer.start()
    }

    override fun onStop() {
        super.onStop()
        timer.cancel()
    }
    */

    override fun onStop() {
        super.onStop()
        emulatorNetworkingService?.stop()
    }

    private fun getField(row: Int, col: Int) : TextView {
        if (fieldSize === FieldSize.THREE) {
            return allFields3x3[row][col]
        } else {
            return allFields5x5[row][col]
        }

    }

    private fun updatePoints() {
        if (fieldSize === FieldSize.THREE) {
            binding3x3.playerOneScore.text = "Punkte x: ${gameManagerTTT.player1Points}"
            binding3x3.playerTwoScore.text = "Punkte o: ${gameManagerTTT.player2Points}"
        } else {
            binding5x5.playerOneScore.text = "Punkte x: ${gameManagerTTT.player1Points}"
            binding5x5.playerTwoScore.text = "Punkte o: ${gameManagerTTT.player2Points}"
        }
    }

    private fun onFieldClick(field: TextView, position: Position){
        if (field.text.isEmpty()) {
            field.text = gameManagerTTT.currentPlayerMark

            // send move to opponent
            sendNetworkMessage("${position.row};${position.column}")

            val winningLine = gameManagerTTT.makeMove(position)
            if (winningLine != null) {
                updatePoints()
                if (fieldSize === FieldSize.THREE) {
                    binding3x3.statusText.text =
                        "Spieler ${gameManagerTTT.currentPlayerMark} hat gewonnen"
                } else {
                    binding5x5.statusText.text =
                        "Spieler ${gameManagerTTT.currentPlayerMark} hat gewonnen"
                }
                disableFields()
                if (fieldSize === FieldSize.THREE) {
                    binding3x3.startNewGameButton.visibility = View.VISIBLE
                } else {
                    binding5x5.startNewGameButton.visibility = View.VISIBLE
                }
                showWinner(winningLine)
                //binding.statusText.visibility = View.GONE
            }
            else {
                if (this.gameMode === GameMode.HARD){
                    ctimer()
                }

                if (fieldSize === FieldSize.THREE) {
                    binding3x3.statusText.text = "Spieler ${gameManagerTTT.currentPlayerMark} ist dran"
                    binding3x3.startNewGameButton.visibility = View.VISIBLE
                } else {
                    binding5x5.statusText.text = "Spieler ${gameManagerTTT.currentPlayerMark} ist dran"
                    binding5x5.startNewGameButton.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun resetFields () {
        if (fieldSize === FieldSize.THREE) {
            allFields3x3.forEach { row ->
                row.forEach {
                    it.text = ""
                    it.background = null
                    it.isEnabled = true
                }
            }
        } else {
            allFields5x5.forEach { row ->
                row.forEach {
                    it.text = ""
                    it.background = null
                    it.isEnabled = true
                }
            }
        }
    }

    private fun disableFields(){
        if (fieldSize === FieldSize.THREE) {
            allFields3x3.forEach { row ->
                row.forEach {
                    it.isEnabled = false
                }
            }
        } else {
            allFields5x5.forEach { row ->
                row.forEach {
                    it.isEnabled = false
                }
            }
        }
    }

    private fun showWinner(winningLine: WinningLine) {
        if (fieldSize === FieldSize.THREE) {
            checkWinner3x3(winningLine)
        } else {
            checkWinner5x5(winningLine)
        }
    }

    private fun checkWinner3x3(winningLine: WinningLine) {
        val (winningFields, background) = when (winningLine) {
            WinningLine.ROW_0 -> Pair(listOf(binding3x3.f0, binding3x3.f1, binding3x3.f2), R.drawable.horizontal_line)
            WinningLine.ROW_1 -> Pair(listOf(binding3x3.f3, binding3x3.f4, binding3x3.f5), R.drawable.horizontal_line)
            WinningLine.ROW_2 -> Pair(listOf(binding3x3.f6, binding3x3.f7, binding3x3.f8), R.drawable.horizontal_line)
            WinningLine.COLUMN_0 -> Pair(listOf(binding3x3.f0, binding3x3.f3, binding3x3.f6), R.drawable.vertical_line)
            WinningLine.COLUMN_1 -> Pair(listOf(binding3x3.f1, binding3x3.f4, binding3x3.f7), R.drawable.vertical_line)
            WinningLine.COLUMN_2 -> Pair(listOf(binding3x3.f2, binding3x3.f5, binding3x3.f8), R.drawable.vertical_line)
            WinningLine.DIAGONAL_LEFT -> Pair(listOf(binding3x3.f0, binding3x3.f4, binding3x3.f8),
                R.drawable.left_diagonal_line
            )
            WinningLine.DIAGONAL_RIGHT -> Pair(listOf(binding3x3.f2, binding3x3.f4, binding3x3.f6),
                R.drawable.right_diagonal_line
            )
        }
        winningFields.forEach { field ->
            field.background = ContextCompat.getDrawable(this, background)
        }
    }
    
    private fun checkWinner5x5(winningLine: WinningLine) {
        // @TODO fix it
        val (winningFields, background) = when (winningLine) {
            WinningLine.ROW_0 -> Pair(listOf(binding5x5.f0, binding5x5.f1, binding5x5.f2), R.drawable.horizontal_line)
            WinningLine.ROW_1 -> Pair(listOf(binding5x5.f3, binding5x5.f4, binding5x5.f5), R.drawable.horizontal_line)
            WinningLine.ROW_2 -> Pair(listOf(binding5x5.f6, binding5x5.f7, binding5x5.f8), R.drawable.horizontal_line)
            WinningLine.COLUMN_0 -> Pair(listOf(binding5x5.f0, binding5x5.f3, binding5x5.f6), R.drawable.vertical_line)
            WinningLine.COLUMN_1 -> Pair(listOf(binding5x5.f1, binding5x5.f4, binding5x5.f7), R.drawable.vertical_line)
            WinningLine.COLUMN_2 -> Pair(listOf(binding5x5.f2, binding5x5.f5, binding5x5.f8), R.drawable.vertical_line)
            WinningLine.DIAGONAL_LEFT -> Pair(listOf(binding5x5.f0, binding5x5.f4, binding5x5.f8),
                R.drawable.left_diagonal_line
            )
            WinningLine.DIAGONAL_RIGHT -> Pair(listOf(binding5x5.f2, binding5x5.f4, binding5x5.f6),
                R.drawable.right_diagonal_line
            )
        }
        winningFields.forEach { field ->
            field.background = ContextCompat.getDrawable(this, background)
        }
    }

    override fun handleNetworkMessage(msg: String) {
        if (DEBUG) Log.d(TAG, "received message: $msg")
        val msgList = msg.split(";")
        val row = msgList[0].toInt()
        val col = msgList[1].toInt()
        onFieldClick(getField(row, col), Position(row, col))
    }
}





/*
private fun onFieldClick(field: TextView) {
    if (field.text == "") {
        field.text = currentPlayer

        if (checkWin()) {
            statusText.text = "Spieler $currentPlayer hat gewonnen"
        } else {
            currentPlayer = if (currentPlayer == "x") "o" else "x"
            statusText.text = "Spieler $currentPlayer ist dran"
        }
    }
}
*/



