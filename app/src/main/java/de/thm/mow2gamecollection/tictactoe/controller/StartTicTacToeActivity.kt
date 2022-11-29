package de.thm.mow2gamecollection.tictactoe.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import de.thm.mow2gamecollection.databinding.ActivityStartTicTacToeBinding
import de.thm.mow2gamecollection.tictactoe.model.GameMode

class StartTicTacToeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartTicTacToeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartTicTacToeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bnt1 = binding.singlePlayerButton
        bnt1.setOnClickListener{
            val intent = Intent(this, TicTacToeActivity::class.java)
            intent.putExtra("gameMode", GameMode.SINGLE.toString())
            startActivity(intent, )
        }

        val btn2 = binding.hotseatButton
        btn2.setOnClickListener{
            val intent = Intent(this, TicTacToeActivity::class.java)
            intent.putExtra("gameMode", GameMode.HOTSEAT.toString())
            startActivity(intent, )
        }
        val btn3 = binding.multiplayerButton
        btn3.setOnClickListener{
            val intent = Intent(this, TicTacToeActivity::class.java)
            intent.putExtra("gameMode", GameMode.NETWORK_MULTIPLAYER.toString())
            startActivity(intent, )
        }
    }
}