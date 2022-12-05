package de.thm.mow2gamecollection.tictactoe.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.tictactoe.model.game.FieldSize
import de.thm.mow2gamecollection.tictactoe.model.game.GameMode

class StartTicTacToeNetworkEmulatorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_tic_tac_toe_network_emulator)

        val intent = Intent(this, TicTacToeActivity::class.java)
        intent.putExtra("gameMode", GameMode.SOFT.toString())
        intent.putExtra("fieldSize", FieldSize.THREE.toString())

        val btn1 = findViewById<Button>(R.id.startServerButton)
        btn1.setOnClickListener{
            intent.putExtra("networkMultiplayerMode", true)
            intent.putExtra("isServer", true)
            intent.putExtra("playerNumber", 1)
            startActivity(intent)
        }
        val btn2 = findViewById<Button>(R.id.startClientButton)
        btn2.setOnClickListener{
            intent.putExtra("networkMultiplayerMode", true)
            intent.putExtra("isServer", false)
            intent.putExtra("playerNumber", 2)
            startActivity(intent)
        }
    }
}