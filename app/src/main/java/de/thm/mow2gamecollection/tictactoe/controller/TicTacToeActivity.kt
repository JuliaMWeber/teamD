package de.thm.mow2gamecollection.tictactoe.controller

import android.icu.text.Transliterator.Position
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.tictactoe.model.GameManagerTTT
import kotlinx.android.synthetic.main.activity_tic_tac_toe.*
import de.thm.mow2gamecollection.tictactoe.model.WinningLine
//import de.thm.mow2gamecollection.tictactoe.model.Position

class TicTacToeActivity : AppCompatActivity() {

    //private var currentPlayer = "x"
    lateinit var model: GameManagerTTT

    private lateinit var gameManagerTTT: GameManagerTTT

    //lateinit var gameManager: GameManager
    private lateinit var f0: TextView
    private lateinit var f1: TextView
    private lateinit var f2: TextView
    private lateinit var f3: TextView
    private lateinit var f4: TextView
    private lateinit var f5: TextView
    private lateinit var f6: TextView
    private lateinit var f7: TextView
    private lateinit var f8: TextView
    private lateinit var startNewGameButton: Button
    private lateinit var player1Points: TextView
    private lateinit var player2Points: TextView

    private lateinit var countdown: TextView
   // private lateinit var timer: CountDownTimer

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tic_tac_toe)

        model = GameManagerTTT(this)

        /*
        val allFields = arrayOf(f0,f1,f2,f3,f4,f5,f6,f7,f8)
        for(field in allFields){
            field.setOnClickListener{
                onFieldClick (it as TextView)
            }
            */

        gameManagerTTT = de.thm.mow2gamecollection.tictactoe.model.GameManagerTTT(this)
        f0 = findViewById(R.id.f0)
        f1 = findViewById(R.id.f1)
        f2 = findViewById(R.id.f2)
        f3 = findViewById(R.id.f3)
        f4 = findViewById(R.id.f4)
        f5 = findViewById(R.id.f5)
        f6 = findViewById(R.id.f6)
        f7 = findViewById(R.id.f7)
        f8 = findViewById(R.id.f8)
        startNewGameButton = findViewById(R.id.startNewGameButton)
        player1Points = findViewById(R.id.player_one_score)
        player2Points = findViewById(R.id.player_two_score)

        f0.setOnClickListener {
            onFieldClick(
                f0,
                de.thm.mow2gamecollection.tictactoe.model.Position(0, 0)
            )
        }
        f1.setOnClickListener {
            onFieldClick(
                f1,
                de.thm.mow2gamecollection.tictactoe.model.Position(0, 1)
            )
        }
        f2.setOnClickListener {
            onFieldClick(
                f2,
                de.thm.mow2gamecollection.tictactoe.model.Position(0, 2)
            )
        }
        f3.setOnClickListener {
            onFieldClick(
                f3,
                de.thm.mow2gamecollection.tictactoe.model.Position(1, 0)
            )
        }
        f4.setOnClickListener {
            onFieldClick(
                f4,
                de.thm.mow2gamecollection.tictactoe.model.Position(1, 1)
            )
        }
        f5.setOnClickListener {
            onFieldClick(
                f5,
                de.thm.mow2gamecollection.tictactoe.model.Position(1, 2)
            )
        }
        f6.setOnClickListener {
            onFieldClick(
                f6,
                de.thm.mow2gamecollection.tictactoe.model.Position(2, 0)
            )
        }
        f7.setOnClickListener {
            onFieldClick(
                f7,
                de.thm.mow2gamecollection.tictactoe.model.Position(2, 1)
            )
        }
        f8.setOnClickListener {
            onFieldClick(
                f8,
                de.thm.mow2gamecollection.tictactoe.model.Position(2, 2)
            )
        }

        startNewGameButton.setOnClickListener {
            startNewGameButton.visibility = View.GONE
            gameManagerTTT.reset()
            resetFields()
            ctimer()
        }
        updatePoints()

    }
    fun ctimer() {
        countdown = findViewById(R.id.countdown)
        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                countdown.setText("übrige Zeit: " + millisUntilFinished / 1000)
            }

            override fun onFinish() {

                countdown.setText("abgelaufen")
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
            Log.d("TicTacToeActivity", "onTick")
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





    private fun updatePoints() {
        player1Points.text = "Punkte x: ${gameManagerTTT.player1Points}"
        player2Points.text = "Punkte o: ${gameManagerTTT.player2Points}"
    }

    private fun onFieldClick(field: TextView, position: de.thm.mow2gamecollection.tictactoe.model.Position){
        if (field.text.isEmpty()) {
            field.text = gameManagerTTT.currentPlayerMark
            statusText.text = "Spieler ${gameManagerTTT.currentPlayerMark} ist dran"
            val winningLine = gameManagerTTT.makeMove(position)
            if (winningLine != null) {
                updatePoints()
                statusText.text = "Spieler ${gameManagerTTT.currentPlayerMark} hat gewonnen"
                disableFields()
                startNewGameButton.visibility = View.VISIBLE
                showWinner(winningLine)
            }else{
                startNewGameButton.visibility = View.VISIBLE
            }
        }
    }
    private fun resetFields () {
        f0.text = ""
        f1.text = ""
        f2.text = ""
        f3.text = ""
        f4.text = ""
        f5.text = ""
        f6.text = ""
        f7.text = ""
        f8.text = ""
        f0.background = null
        f1.background = null
        f2.background = null
        f3.background = null
        f4.background = null
        f5.background = null
        f6.background = null
        f7.background = null
        f8.background = null
        f0.isEnabled = true
        f1.isEnabled = true
        f2.isEnabled = true
        f3.isEnabled = true
        f4.isEnabled = true
        f5.isEnabled = true
        f6.isEnabled = true
        f7.isEnabled = true
        f8.isEnabled = true
    }
    private fun disableFields(){
        f0.isEnabled = false
        f1.isEnabled = false
        f2.isEnabled = false
        f3.isEnabled = false
        f4.isEnabled = false
        f5.isEnabled = false
        f6.isEnabled = false
        f7.isEnabled = false
        f8.isEnabled = false
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
            field.background = ContextCompat.getDrawable(TicTacToeActivity@this, background)
        }
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



