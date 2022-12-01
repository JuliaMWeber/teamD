package de.thm.mow2gamecollection.tictactoe.controller

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import de.thm.mow2gamecollection.R
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
    private lateinit var binding: ActivityTicTacToeBinding
    private lateinit var gameManagerTTT: GameManagerTTT

    private lateinit var gameMode: GameMode;

    //private var currentPlayer = "x"
    private var playerNumber: Int? = null
    private val allFields by lazy {
        arrayOf(
            arrayOf(binding.f0, binding.f1, binding.f2),
            arrayOf(binding.f3, binding.f4, binding.f5),
            arrayOf(binding.f6, binding.f7, binding.f8)
        )
    }


    // lateinit var gameManager: GameManager
    // private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gameModeString: String? = intent.getStringExtra("gameMode")
        this.gameMode = gameModeString?.let { GameMode.valueOf(it) }!!

        gameManagerTTT = GameManagerTTT(this)
        binding = ActivityTicTacToeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val networkMultiplayerMode = intent.getBooleanExtra("networkMultiplayerMode", false)
        if (DEBUG) Log.d(TAG, "networkMultiplayerMode: $networkMultiplayerMode")
        if(networkMultiplayerMode) {
            playerNumber = intent.getIntExtra("playerNumber", 0)
            if (DEBUG) Log.d(TAG, "playerNumber: $playerNumber")
            initEmulatorNetworkingService(this, intent.getBooleanExtra("isServer", false))
        }

        /*
        for(field in allFields){
            field.setOnClickListener{
                onFieldClick (it as TextView)
            }
            */

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


        binding.startNewGameButton.setOnClickListener {
            it.visibility = View.GONE
            gameManagerTTT.reset()
            resetFields()
           // if(btn2.setOnClickListener(this))
             //   //ctimer()
        }
        updatePoints()

    }
    private fun ctimer() {
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                binding.countdown.text = "übrige Zeit: ${millisUntilFinished / 1000}"
            }

            override fun onFinish() {

                binding.countdown.text = "Zeit abgelaufen"
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
        return allFields[row][col]
    }

    private fun updatePoints() {
        binding.playerOneScore.text = "Punkte x: ${gameManagerTTT.player1Points}"
        binding.playerTwoScore.text = "Punkte o: ${gameManagerTTT.player2Points}"
    }

    private fun onFieldClick(field: TextView, position: Position){
        if (field.text.isEmpty()) {
            field.text = gameManagerTTT.currentPlayerMark

            // send move to opponent
            sendNetworkMessage("${position.row};${position.column}")

            val winningLine = gameManagerTTT.makeMove(position)
            if (winningLine != null) {
                updatePoints()
                binding.statusText.text = "Spieler ${gameManagerTTT.currentPlayerMark} hat gewonnen"
                disableFields()
                binding.startNewGameButton.visibility = View.VISIBLE
                showWinner(winningLine)
                //binding.statusText.visibility = View.GONE
            }else{
                if (this.gameMode === GameMode.HARD){
                    ctimer()
                }
                binding.statusText.text = "Spieler ${gameManagerTTT.currentPlayerMark} ist dran"
                binding.startNewGameButton.visibility = View.VISIBLE
            }
        }
    }
    private fun resetFields () {
        allFields.forEach { row ->
            row.forEach {
                it.text = ""
                it.background = null
                it.isEnabled = true
            }
        }
    }
    private fun disableFields(){
        allFields.forEach { row ->
            row.forEach {
                it.isEnabled = false
            }
        }
    }

    private fun showWinner(winningLine: WinningLine) {
        val (winningFields, background) = when (winningLine) {
            WinningLine.ROW_0 -> Pair(listOf(binding.f0, binding.f1, binding.f2), R.drawable.horizontal_line)
            WinningLine.ROW_1 -> Pair(listOf(binding.f3, binding.f4, binding.f5), R.drawable.horizontal_line)
            WinningLine.ROW_2 -> Pair(listOf(binding.f6, binding.f7, binding.f8), R.drawable.horizontal_line)
            WinningLine.COLUMN_0 -> Pair(listOf(binding.f0, binding.f3, binding.f6), R.drawable.vertical_line)
            WinningLine.COLUMN_1 -> Pair(listOf(binding.f1, binding.f4, binding.f7), R.drawable.vertical_line)
            WinningLine.COLUMN_2 -> Pair(listOf(binding.f2, binding.f5, binding.f8), R.drawable.vertical_line)
            WinningLine.DIAGONAL_LEFT -> Pair(listOf(binding.f0, binding.f4, binding.f8),
                R.drawable.left_diagonal_line
            )
            WinningLine.DIAGONAL_RIGHT -> Pair(listOf(binding.f2, binding.f4, binding.f6),
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



