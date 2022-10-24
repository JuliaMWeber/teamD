package de.thm.mow2gamecollection

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import de.thm.mow2gamecollection.wordle.controller.WordleActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.startWordleButton).setOnClickListener() {
            val intent = Intent(this, WordleActivity::class.java)
            startActivity(intent)
        }
    }
}