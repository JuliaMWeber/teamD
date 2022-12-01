package de.thm.mow2gamecollection.tictactoe.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.controller.GamesListActivity
import de.thm.mow2gamecollection.databinding.ActivityStartTicTacToeBinding
import de.thm.mow2gamecollection.databinding.ActivityTicTacToeBinding
import de.thm.mow2gamecollection.tictactoe.model.GameManagerTTT

class StartTicTacToeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartTicTacToeBinding
    //private lateinit var gameManagerTTT: GameManagerTTT


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_tic_tac_toe)

        val btn1 = findViewById<Button>(R.id.easyButton)
        btn1.setOnClickListener{
            val intent = Intent(this, TicTacToeActivity::class.java)
            intent.putExtra("gameMode", GameMode.SOFT.toString())
            intent.putExtra("fieldSize", FieldSize.THREE.toString())
            startActivity(intent, )
        }
        val btn2 = findViewById<Button>(R.id.hardButton)
        btn2.setOnClickListener{
            val intent = Intent(this, TicTacToeActivity::class.java)
            intent.putExtra("gameMode", GameMode.HARD.toString())
            intent.putExtra("fieldSize", FieldSize.THREE.toString())
            startActivity(intent, )
        }
        val btn3 = findViewById<Button>(R.id.easyButton5x5)
        btn3.setOnClickListener{
            val intent = Intent(this, TicTacToeActivity::class.java)
            intent.putExtra("gameMode", GameMode.SOFT.toString())
            intent.putExtra("fieldSize", FieldSize.FIVE.toString())
            startActivity(intent, )
        }
        val btn4 = findViewById<Button>(R.id.hardButton5x5)
        btn4.setOnClickListener{
            val intent = Intent(this, TicTacToeActivity::class.java)
            intent.putExtra("gameMode", GameMode.HARD.toString())
            intent.putExtra("fieldSize", FieldSize.FIVE.toString())
            startActivity(intent, )
        }


    }
}