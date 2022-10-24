package de.thm.mow2gamecollection.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import de.thm.mow2gamecollection.R

class MainActivity : AppCompatActivity() {
    //lateinit var btn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.startButton)
        btn.setOnClickListener{
            val Intent = Intent(this, GamesListActivity::class.java)
            startActivity(Intent)
        }
    }


}

