package de.thm.mow2gamecollection.controller

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import de.thm.mow2gamecollection.R

private const val TAG = "StartChatActivity"
class StartChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_chat)

        val intent = Intent(this, ChatActivity::class.java)

        val btn1 = findViewById<Button>(R.id.startServerButton)
        btn1.setOnClickListener{
            intent.putExtra("isServer", true)
            startActivity(intent)
        }
        val btn2 = findViewById<Button>(R.id.startClientButton)
        btn2.setOnClickListener{
            intent.putExtra("isServer", false)
            startActivity(intent)
        }
    }
}