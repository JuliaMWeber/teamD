package de.thm.mow2gamecollection.controller

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.tictactoe.controller.TicTacToeActivity

private const val TAG = "StartChatActivity"
class StartTicTacToeNetworkMultiplayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_chat)

        val intent = Intent(this, TicTacToeActivity::class.java)

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