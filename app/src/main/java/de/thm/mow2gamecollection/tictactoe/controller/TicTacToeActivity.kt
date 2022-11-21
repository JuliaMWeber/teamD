package de.thm.mow2gamecollection.tictactoe.controller

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.model.EmulatorEnabledMultiplayerGame
import de.thm.mow2gamecollection.service.EmulatorNetworkingService
import de.thm.mow2gamecollection.tictactoe.model.GameManagerTTT
import de.thm.mow2gamecollection.tictactoe.model.Position
import kotlinx.android.synthetic.main.activity_tic_tac_toe.*
import de.thm.mow2gamecollection.tictactoe.model.WinningLine
//import de.thm.mow2gamecollection.tictactoe.model.Position

// DEBUGGING
private const val TAG = "TicTacToeActivity"
private const val DEBUG = false // set to true to print debug logs

class TicTacToeActivity : AppCompatActivity(), EmulatorEnabledMultiplayerGame {

    override var emulatorNetworkingService: EmulatorNetworkingService? = null

    //private var currentPlayer = "x"
    lateinit var model: GameManagerTTT

    private lateinit var gameManagerTTT: GameManagerTTT

    private var playerNumber: Int? = null

    //lateinit var gameManager: GameManager
    private lateinit var allFields: Array<Array<TextView>>

   // private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val networkMultiplayerMode = intent.getBooleanExtra("networkMultiplayerMode", false)
        if (DEBUG) Log.d(TAG, "networkMultiplayerMode: $networkMultiplayerMode")
        if(networkMultiplayerMode) {
            playerNumber = intent.getIntExtra("playerNumber", 0)
            if (DEBUG) Log.d(TAG, "playerNumber: $playerNumber")
            initEmulatorNetworkingService(this, intent.getBooleanExtra("isServer", false))
        }

        setContentView(R.layout.activity_tic_tac_toe)

        model = GameManagerTTT(this)

        /*
        for(field in allFields){
            field.setOnClickListener{
                onFieldClick (it as TextView)
            }
            */

        gameManagerTTT = GameManagerTTT(this)
        allFields = arrayOf(
            arrayOf(f0,f1,f2),
            arrayOf(f3,f4,f5),
            arrayOf(f6,f7,f8)
        )

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

        startNewGameButton.setOnClickListener {
            startNewGameButton.visibility = View.GONE
            gameManagerTTT.reset()
            resetFields()
            ctimer()
        }
        updatePoints()

    }
    private fun ctimer() {
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                countdown.text = "übrige Zeit: ${millisUntilFinished / 1000}"
            }

            override fun onFinish() {

                countdown.text = "abgelaufen"
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
        playerOneScore.text = "Punkte x: ${gameManagerTTT.player1Points}"
        playerTwoScore.text = "Punkte o: ${gameManagerTTT.player2Points}"
    }

    private fun onFieldClick(field: TextView, position: de.thm.mow2gamecollection.tictactoe.model.Position){
        if (field.text.isEmpty()) {
            field.text = gameManagerTTT.currentPlayerMark

            // send move to opponent
            sendNetworkMessage("${position.row};${position.column}")

            val winningLine = gameManagerTTT.makeMove(position)
            if (winningLine != null) {
                updatePoints()
                statusText.text = "Spieler ${gameManagerTTT.currentPlayerMark} hat gewonnen"
                disableFields()
                startNewGameButton.visibility = View.VISIBLE
                showWinner(winningLine)
            }else{
                statusText.text = "Spieler ${gameManagerTTT.currentPlayerMark} ist dran"
                startNewGameButton.visibility = View.VISIBLE
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
            WinningLine.ROW_0 -> Pair(listOf(f0,f1,f2), R.drawable.horizontal_line)
            WinningLine.ROW_1 -> Pair(listOf(f3,f4,f5), R.drawable.horizontal_line)
            WinningLine.ROW_2 -> Pair(listOf(f6,f7,f8), R.drawable.horizontal_line)
            WinningLine.COLUMN_0 -> Pair(listOf(f0,f3,f6), R.drawable.vertical_line)
            WinningLine.COLUMN_1 -> Pair(listOf(f1,f4,f7), R.drawable.vertical_line)
            WinningLine.COLUMN_2 -> Pair(listOf(f2,f5,f8), R.drawable.vertical_line)
            WinningLine.DIAGONAL_LEFT -> Pair(listOf(f0, f4, f8),
                R.drawable.left_diagonal_line
            )
            WinningLine.DIAGONAL_RIGHT -> Pair(listOf(f2, f4, f6),
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



