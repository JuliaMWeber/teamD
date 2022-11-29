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
import de.thm.mow2gamecollection.tictactoe.model.GameManagerTTT
import de.thm.mow2gamecollection.tictactoe.model.GameMode
import de.thm.mow2gamecollection.tictactoe.model.Position
import de.thm.mow2gamecollection.tictactoe.model.WinningLine

// DEBUGGING
private const val TAG = "TicTacToeActivity"
private const val DEBUG = false // set to true to print debug logs


class TicTacToeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTicTacToeBinding
    private lateinit var gameManagerTTT: GameManagerTTT

    //changing between easy and hard
    lateinit var gameMode: GameMode

    //private var currentPlayer = "x"
    private var playerNumber: Int? = null
    private val allFields by lazy {
        arrayOf(
            arrayOf(binding.f0, binding.f1, binding.f2),
            arrayOf(binding.f3, binding.f4, binding.f5),
            arrayOf(binding.f6, binding.f7, binding.f8)
        )
    }

    private lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.getStringExtra("gameMode")?.let {
            gameMode = GameMode.valueOf(it)
        } ?: run {
            finish()
        }

        binding = ActivityTicTacToeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gameManagerTTT = GameManagerTTT(this, gameMode)

        if(gameMode == GameMode.NETWORK_MULTIPLAYER) {
            playerNumber = intent.getIntExtra("playerNumber", 0)
            if (DEBUG) Log.d(TAG, "playerNumber: $playerNumber")
            gameManagerTTT.initEmulatorNetworkingService(this, intent.getBooleanExtra("isServer", false))
        }

        initializeFields()

        //if (this.gameMode === GameMode.SINGLE){
        //    autoplay()
        //}

        updatePoints()
    }

    private fun initializeFields() {
        for (i in allFields.indices) {
            val row = allFields[i]
            for (j in row.indices) {
                val field = row[j]
                field.setOnClickListener {
                    onFieldClick(
                        Position(i, j)
                    )
                }
            }
        }

        binding.startNewGameButton.setOnClickListener {
            it.visibility = View.GONE
            gameManagerTTT.reset()
            resetFields()
        }
    }

    private fun ctimer() {
        countDownTimer = object : CountDownTimer(5000, 1000) {
        //object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.d(TAG, "onTick")
                binding.countdown.text = "übrige Zeit: ${millisUntilFinished / 1000}"

            }

            override fun onFinish() {
                Log.d(TAG, "onFinish")
                binding.countdown.text = "Zeit abgelaufen"
                //resetFields()
                //updatePoints()
            }
        }.start()
    }
/*
    var player1 = ArrayList<Int>()
    var player2 = ArrayList<Int>()

    private fun autoplay() {
        var emptyCells = ArrayList<Int>()

        val rnd = (1..9).random()
        if (emptyCells.contains(rnd)) {
            autoplay()
        } else {
            val fieldSelected : TextView
            fieldSelected = when(rnd) {
                1 -> f0
                2 -> f1
                3 -> f2
                4 -> f3
                5 -> f4
                6 -> f5
                7 -> f6
                8 -> f7
                9 -> f8
                else -> {
                    f0
                }
            }
            emptyCells.add(rnd)
            fieldSelected.text = "O"
            fieldSelected.setTextColor(Color.parseColor("#D22BB804"))
            player2.add(rnd)
            fieldSelected.isEnabled = false

           // var checkWinner =  checkWinner()
           // if (checkWinner == 1)
            //    Handler().postDelayed(Runnable { reset() }, 2000)
        }
    }


 */

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
        if (gameMode == GameMode.NETWORK_MULTIPLAYER) {
            gameManagerTTT.emulatorNetworkingService?.stop()
        }
    }

    private fun getField(row: Int, col: Int) : TextView {
        return allFields[row][col]
    }

    private fun updatePoints() {
        binding.playerOneScore.text = "Punkte x: ${gameManagerTTT.player1Points}"
        binding.playerTwoScore.text = "Punkte o: ${gameManagerTTT.player2Points}"
    }

    fun onFieldClick(position: Position){
        Log.d(TAG, "onFieldClick ${position}")
        val field = getField(position.row, position.column)
        if (field.text.isEmpty()) {
            field.text = gameManagerTTT.currentPlayerMark

            val winningLine = gameManagerTTT.makeMove(position)
            if (winningLine != null) {
                updatePoints()
                binding.statusText.text = "Spieler ${gameManagerTTT.currentPlayerMark} hat gewonnen"
                disableFields()
                binding.startNewGameButton.visibility = View.VISIBLE
                showWinner(winningLine)
                //binding.statusText.visibility = View.GONE
            } else {
                ctimer()
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
}




