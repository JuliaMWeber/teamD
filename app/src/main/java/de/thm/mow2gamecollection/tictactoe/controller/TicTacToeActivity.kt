package de.thm.mow2gamecollection.tictactoe.controller

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.databinding.ActivityTicTacToe5x5Binding
import de.thm.mow2gamecollection.databinding.ActivityTicTacToeBinding
import de.thm.mow2gamecollection.model.EmulatorEnabledMultiplayerGame
import de.thm.mow2gamecollection.service.EmulatorNetworkingService
import de.thm.mow2gamecollection.tictactoe.model.*

// DEBUGGING
private const val TAG = "TicTacToeActivity"
private const val DEBUG = false // set to true to print debug logs


class TicTacToeActivity : AppCompatActivity(), EmulatorEnabledMultiplayerGame {

    override var emulatorNetworkingService: EmulatorNetworkingService? = null

    private lateinit var binding3x3: ActivityTicTacToeBinding
    private lateinit var binding5x5: ActivityTicTacToe5x5Binding

    private lateinit var fieldSize: FieldSize

    private lateinit var gameManagerTTT: GameManagerTicTacToe
    private lateinit var gameMode: GameMode
    private lateinit var roundTimer: CountDownTimer

    private var playerNumber: Int? = null
    private val allFields3x3 by lazy {
        arrayOf(
            arrayOf(binding3x3.f0, binding3x3.f1, binding3x3.f2),
            arrayOf(binding3x3.f3, binding3x3.f4, binding3x3.f5),
            arrayOf(binding3x3.f6, binding3x3.f7, binding3x3.f8)
        )
    }
    private val allFields5x5 by lazy {
        arrayOf(
            arrayOf(binding5x5.f0, binding5x5.f1, binding5x5.f2, binding5x5.f01, binding5x5.f02),
            arrayOf(binding5x5.f3, binding5x5.f4, binding5x5.f5, binding5x5.f11, binding5x5.f12),
            arrayOf(binding5x5.f6, binding5x5.f7, binding5x5.f8, binding5x5.f20, binding5x5.f21),
            arrayOf(binding5x5.f30, binding5x5.f31, binding5x5.f32, binding5x5.f33, binding5x5.f34),
            arrayOf(binding5x5.f40, binding5x5.f41, binding5x5.f42, binding5x5.f43, binding5x5.f44)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gameModeString: String? = intent.getStringExtra("gameMode")
        this.gameMode = gameModeString?.let { GameMode.valueOf(it) }!!

        gameManagerTTT = GameManagerTicTacToe(this)
        if(fieldSize==FieldSize.THREE){
            binding3x3 = ActivityTicTacToeBinding.inflate(layoutInflater)
            setContentView(binding3x3.root)
        } else {
            binding5x5 = ActivityTicTacToeBinding5x5.inflate(layoutInflater)
            setContentView(binding5x5.root)
        }

        val networkMultiplayerMode = intent.getBooleanExtra("networkMultiplayerMode", false)
        if (DEBUG) Log.d(TAG, "networkMultiplayerMode: $networkMultiplayerMode")
        if (networkMultiplayerMode) {
            playerNumber = intent.getIntExtra("playerNumber", 0)
            if (DEBUG) Log.d(TAG, "playerNumber: $playerNumber")
            initEmulatorNetworkingService(this, intent.getBooleanExtra("isServer", false))
        }
        initializeFields()
        initTimer()
    }

    fun getFieldsize(): FieldSize {
        return fieldSize
    }

    fun initializeFields() {
        if(fieldSize==FieldSize.THREE){
            initializeFields3x3()
        } else {
            initializeFields5x5()
        }
    }

    fun initializeFields3x3() {
        for (i in allFields3x3.indices) {
            val row = allFields3x3[i]
            for (j in row.indices) {
                val field = row[j]
                field.setOnClickListener {
                    onFieldClick(
                        Position(i, j)
                    )
                }
            }
        }
        binding3x3.startNewGameButton.setOnClickListener {
            it.visibility = View.GONE
            gameManagerTTT.resetGrid()
            resetFields()
            if (this.gameMode === GameMode.HARD) {
                roundTimer.cancel()
                roundTimer.onFinish()
                roundTimer.start()
            }
        }
    }

    fun initializeFields5x5() {
        for (i in allFields5x5.indices) {
            val row = allFields5x5[i]
            for (j in row.indices) {
                val field = row[j]
                field.setOnClickListener {
                    onFieldClick(
                        Position(i, j)
                    )
                }
            }
        }
        binding5x5.startNewGameButton.setOnClickListener {
            it.visibility = View.GONE
            gameManagerTTT.resetGrid()
            resetFields()
            if (this.gameMode === GameMode.HARD) {
                roundTimer.cancel()
                roundTimer.onFinish()
                roundTimer.start()
            }
        }
    }

    private fun initTimer() {
        if(fieldSize==FieldSize.THREE){
            initTimer3x3()
        } else {
            initTimer5x5()
        }
    }

    private fun initTimer3x3() {
        roundTimer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding3x3.countdown.text = "übrige Zeit: ${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                binding3x3.countdown.text = "Zeit abgelaufen"
                gameManagerTTT.changeActivePlayer()
            }
        }
    }

    private fun initTimer5x5() {
        roundTimer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding5x5.countdown.text = "übrige Zeit: ${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                binding5x5.countdown.text = "Zeit abgelaufen"
                gameManagerTTT.changeActivePlayer()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        emulatorNetworkingService?.stop()
    }

    private fun getField(row: Int, col: Int) : TextView {
        if(fieldSize==FieldSize.THREE){
            return getField3x3(row, col)
        } else {
            return getField5x5(row, col)
        }
    }

    private fun getField3x3(row: Int, col: Int) : TextView {
        return allFields3x3[row][col]
    }

    private fun getField5x5(row: Int, col: Int) : TextView {
        return allFields5x5[row][col]
    }

    fun updatePoints() {
        if(fieldSize==FieldSize.THREE){
            updatePoints3x3()
        } else {
            updatePoints5x5()
        }
    }

    fun updatePoints3x3() {
        binding3x3.playerOneScore.text = "Punkte x: ${gameManagerTTT.player1Points}"
        binding3x3.playerTwoScore.text = "Punkte o: ${gameManagerTTT.player2Points}"
    }

    fun updatePoints5x5() {
        binding5x5.playerOneScore.text = "Punkte x: ${gameManagerTTT.player1Points}"
        binding5x5.playerTwoScore.text = "Punkte o: ${gameManagerTTT.player2Points}"
    }

    private fun onFieldClick(position: Position){
        Log.d(TAG, "onFieldClick ${position}")
        val field = getField(position.row,position.column)
        if (field.text.isEmpty()) {
            field.text = gameManagerTTT.currentPlayerMark

            // send move to opponent
            sendNetworkMessage("${position.row};${position.column}")

            if(fieldSize==FieldSize.THREE){
                val winningLine = gameManagerTTT.makeMove3x3(position)
                if (winningLine != null && winningLine != WinningLine3x3.NOWINNER) {
                    binding3x3.statusText.text = "Spieler ${gameManagerTTT.currentPlayerMark} hat gewonnen"
                    disableFields()
                    binding3x3.startNewGameButton.visibility = View.VISIBLE
                    showWinner3x3(winningLine)
                } else if (winningLine == WinningLine3x3.NOWINNER) {
                    binding3x3.statusText.text = "Game over"
                    disableFields()
                    if (this.gameMode === GameMode.HARD) {
                        roundTimer.cancel()
                    }
                    binding3x3.startNewGameButton.visibility = View.VISIBLE
                } else {
                    if (this.gameMode === GameMode.HARD){
                        roundTimer.start()
                    }
                    showActivePlayer()
                }
            } else {
                val winningLine = gameManagerTTT.makeMove5x5(position)
                if (winningLine != null && winningLine != WinningLine5x5.NOWINNER) {
                    binding5x5.statusText.text = "Spieler ${gameManagerTTT.currentPlayerMark} hat gewonnen"
                    disableFields()
                    binding5x5.startNewGameButton.visibility = View.VISIBLE
                    showWinner5x5(winningLine)
                } else if (winningLine == WinningLine5x5.NOWINNER) {
                    binding5x5.statusText.text = "Game over"
                    disableFields()
                    if (this.gameMode === GameMode.HARD) {
                        roundTimer.cancel()
                    }
                    binding5x5.startNewGameButton.visibility = View.VISIBLE
                } else {
                    if (this.gameMode === GameMode.HARD){
                        roundTimer.start()
                    }
                    showActivePlayer()
                }
            }
        }
    }

    fun showActivePlayer() {
        if(fieldSize==FieldSize.THREE){
            showActivePlayer3x3()
        } else {
            showActivePlayer5x5()
        }
    }

    fun showActivePlayer3x3() {
        if (gameManagerTTT.currentPlayerMark == "x") {
            binding3x3.XTurn.visibility = View.VISIBLE
            binding3x3.OTurn.visibility = View.INVISIBLE
        } else {
            binding3x3.XTurn.visibility = View.INVISIBLE
            binding3x3.OTurn.visibility = View.VISIBLE
        }
        binding3x3.statusText.text = "Spieler ${gameManagerTTT.currentPlayerMark} ist dran"
        binding3x3.startNewGameButton.visibility = View.VISIBLE
    }

    fun showActivePlayer5x5() {
        if (gameManagerTTT.currentPlayerMark == "x") {
            binding5x5.XTurn.visibility = View.VISIBLE
            binding5x5.OTurn.visibility = View.INVISIBLE
        } else {
            binding5x5.XTurn.visibility = View.INVISIBLE
            binding5x5.OTurn.visibility = View.VISIBLE
        }
        binding5x5.statusText.text = "Spieler ${gameManagerTTT.currentPlayerMark} ist dran"
        binding5x5.startNewGameButton.visibility = View.VISIBLE
    }

    fun getGameMode() :GameMode {
        return this.gameMode
    }

    fun restartTimer() {
        roundTimer.cancel()
        roundTimer.start()
    }

    private fun resetFields () {
        if(fieldSize==FieldSize.THREE){
            resetFields3x3()
        } else {
            resetFields5x5()
        }
    }

    private fun resetFields3x3 () {
        allFields3x3.forEach { row ->
            row.forEach {
                it.text = ""
                it.background = null
                it.isEnabled = true
            }
        }
    }

    private fun resetFields5x5 () {
        allFields5x5.forEach { row ->
            row.forEach {
                it.text = ""
                it.background = null
                it.isEnabled = true
            }
        }
    }

    private fun disableFields(){
        if(fieldSize==FieldSize.THREE){
            disableFields3x3()
        } else {
            disableFields5x5()
        }
    }

    private fun disableFields3x3(){
        allFields3x3.forEach { row ->
            row.forEach {
                it.isEnabled = false
            }
        }
    }

    private fun disableFields5x5(){
        allFields5x5.forEach { row ->
            row.forEach {
                it.isEnabled = false
            }
        }
    }

    private fun showWinner3x3(winningLine3x3: WinningLine3x3) {
        if (this.gameMode === GameMode.HARD) {
            roundTimer.cancel()
        }
        val (winningFields, background) = when (winningLine3x3) {
            WinningLine3x3.ROW_0 -> Pair(listOf(binding3x3.f0, binding3x3.f1, binding3x3.f2), R.drawable.horizontal_line)
            WinningLine3x3.ROW_1 -> Pair(listOf(binding3x3.f3, binding3x3.f4, binding3x3.f5), R.drawable.horizontal_line)
            WinningLine3x3.ROW_2 -> Pair(listOf(binding3x3.f6, binding3x3.f7, binding3x3.f8), R.drawable.horizontal_line)
            WinningLine3x3.COLUMN_0 -> Pair(listOf(binding3x3.f0, binding3x3.f3, binding3x3.f6), R.drawable.vertical_line)
            WinningLine3x3.COLUMN_1 -> Pair(listOf(binding3x3.f1, binding3x3.f4, binding3x3.f7), R.drawable.vertical_line)
            WinningLine3x3.COLUMN_2 -> Pair(listOf(binding3x3.f2, binding3x3.f5, binding3x3.f8), R.drawable.vertical_line)
            WinningLine3x3.DIAGONAL_LEFT -> Pair(listOf(binding3x3.f0, binding3x3.f4, binding3x3.f8),
                R.drawable.left_diagonal_line
            )
            WinningLine3x3.DIAGONAL_RIGHT -> Pair(listOf(binding3x3.f2, binding3x3.f4, binding3x3.f6),
                R.drawable.right_diagonal_line
            )
            WinningLine3x3.NOWINNER -> Pair(listOf(binding3x3.f0,binding3x3.f1, binding3x3.f2, binding3x3.f3, binding3x3.f4, binding3x3.f5, binding3x3.f6, binding3x3.f7, binding3x3.f8),
                R.drawable.horizontal_line
            )
        }
        winningFields.forEach { field ->
            field.background = ContextCompat.getDrawable(this, background)
        }
    }
    
    private fun showWinner5x5(winningLine5x5: WinningLine5x5) {
        if (this.gameMode === GameMode.HARD) {
            roundTimer.cancel()
        }
        val (winningFields, background) = when (winningLine5x5) {
            WinningLine5x5.ROW_0 -> Pair(listOf(binding5x5.f0, binding5x5.f1, binding5x5.f2, binding5x5.f01, binding5x5.f02), R.drawable.horizontal_line)
            WinningLine5x5.ROW_1 -> Pair(listOf(binding5x5.f3, binding5x5.f4, binding5x5.f5, binding5x5.f11, binding5x5.f12), R.drawable.horizontal_line)
            WinningLine5x5.ROW_2 -> Pair(listOf(binding5x5.f6, binding5x5.f7, binding5x5.f8, binding5x5.f20, binding5x5.f21), R.drawable.horizontal_line)
            WinningLine5x5.ROW_3 -> Pair(listOf(binding5x5.f30, binding5x5.f31, binding5x5.f32, binding5x5.f33, binding5x5.f34), R.drawable.horizontal_line)
            WinningLine5x5.ROW_4 -> Pair(listOf(binding5x5.f40, binding5x5.f41, binding5x5.f42, binding5x5.f43, binding5x5.f44), R.drawable.horizontal_line)
            WinningLine5x5.COLUMN_0 -> Pair(listOf(binding5x5.f0, binding5x5.f3, binding5x5.f6,binding5x5.f30, binding5x5.f40), R.drawable.vertical_line)
            WinningLine5x5.COLUMN_1 -> Pair(listOf(binding5x5.f1, binding5x5.f4, binding5x5.f7,binding5x5.f31,binding5x5.f41), R.drawable.vertical_line)
            WinningLine5x5.COLUMN_2 -> Pair(listOf(binding5x5.f2, binding5x5.f5, binding5x5.f8,binding5x5.f32,binding5x5.f42), R.drawable.vertical_line)
            WinningLine5x5.COLUMN_3 -> Pair(listOf(binding5x5.f01, binding5x5.f11, binding5x5.f20,binding5x5.f33,binding5x5.f43), R.drawable.vertical_line)
            WinningLine5x5.COLUMN_4 -> Pair(listOf(binding5x5.f02, binding5x5.f12, binding5x5.f21,binding5x5.f34,binding5x5.f44), R.drawable.vertical_line)
            WinningLine5x5.DIAGONAL_LEFT -> Pair(listOf(binding5x5.f0, binding5x5.f4, binding5x5.f8,binding5x5.f33,binding5x5.f44),
                R.drawable.left_diagonal_line
            )
            WinningLine5x5.DIAGONAL_RIGHT -> Pair(listOf(binding5x5.f02, binding5x5.f11, binding5x5.f8,binding5x5.f31,binding5x5.f40),
                R.drawable.right_diagonal_line
            )
            WinningLine5x5.NOWINNER -> Pair(listOf(binding3x3.f0,binding3x3.f1, binding3x3.f2, binding3x3.f3, binding3x3.f4, binding3x3.f5, binding3x3.f6, binding3x3.f7, binding3x3.f8),
                R.drawable.horizontal_line
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
        //onFieldClick(getField(row, col), Position(row, col))
        onFieldClick(Position(row, col))
    }
}
