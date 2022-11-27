package de.thm.mow2gamecollection.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.databinding.ActivityMainBinding
import de.thm.mow2gamecollection.wordle.controller.LetterGridFragment
import de.thm.mow2gamecollection.wordle.model.grid.LetterStatus

// DEBUGGING
private const val DEBUG = true
private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), KeyboardActivity {
    private lateinit var binding: ActivityMainBinding
    private var playerName = ""
    private val nameInput: LetterGridFragment by lazy { binding.nameInput.getFragment() }
    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startButton.setOnClickListener{
            Log.d(TAG, "startButton")
            val intent = Intent(this@MainActivity, GamesListActivity::class.java)
            startActivity(intent)
        }
    }

    override fun handleKeyboardClick(button: Button) {
        when (button.text) {
            "✓" -> {
                // TODO
            }
            "←" -> {
                if (index > 0) {
                    nameInput.removeLetter(0, --index)
                    playerName = playerName.dropLast(1)
                    if (DEBUG) Log.d(TAG, "playerName: $playerName (${index})")
                }
            }
            else -> {
                nameInput.updateTile(0, index, button.text.first(), LetterStatus.UNKNOWN)
                playerName += button.text

                if (DEBUG) Log.d(TAG, "playerName: $playerName (${index+1})")
                index++
            }
        }
    }
}

