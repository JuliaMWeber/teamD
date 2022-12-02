package de.thm.mow2gamecollection.controller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.databinding.ActivityMainBinding
import de.thm.mow2gamecollection.model.FirstRunModel
import de.thm.mow2gamecollection.model.UserSettings
import de.thm.mow2gamecollection.wordle.controller.LetterGridFragment
import de.thm.mow2gamecollection.wordle.model.grid.LetterStatus

// DEBUGGING
private const val DEBUG = true
private const val TAG = "FirstRunActivity"

class FirstRunActivity : KeyboardActivity() {
    private lateinit var model: FirstRunModel
    private lateinit var binding: ActivityMainBinding
    private var playerName = ""
    private val nameInput: LetterGridFragment by lazy { binding.nameInput.getFragment() }
    private var index = 0
    private val maxUserNameLength: Int by lazy {
        resources.getInteger(R.integer.max_user_name_length)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        LetterGridFragment.newInstance(1, maxUserNameLength).let{
            supportFragmentManager.beginTransaction()
                .add(R.id.nameInput, it)
                .commit()
        }

        binding.startButton.setOnClickListener{
            if(playerName.isNotEmpty()) {
                PreferenceManager.getDefaultSharedPreferences(this).edit().apply {
                    putBoolean(UserSettings.IS_FIRST_RUN_KEY, false)
                }.apply()
                model.saveUserName(playerName)
                val intent = Intent(this@FirstRunActivity, GamesListActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "saved userName: ${UserSettings.getUserName(this)}")
        model = FirstRunModel(this)
    }

    override fun addLetter(char: Char) {
        if (index < maxUserNameLength) {
            nameInput.updateTile(0, index, char, LetterStatus.UNKNOWN)
            playerName += char

            if (DEBUG) Log.d(TAG, "playerName: $playerName (${index+1})")
            index++
        }
    }

    override fun removeLetter() {
        if (index > 0) {
            nameInput.removeLetter(0, --index)
            playerName = playerName.dropLast(1)
            if (DEBUG) Log.d(TAG, "playerName: $playerName (${index})")
        }
    }

    override fun submit() {
        if (DEBUG) Log.d(TAG, "submit")
        // TODO
    }
}

