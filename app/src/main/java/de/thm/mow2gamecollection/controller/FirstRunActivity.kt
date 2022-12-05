package de.thm.mow2gamecollection.controller

import android.content.Intent
import android.os.Bundle
import androidx.preference.PreferenceManager
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.model.FirstRunModel
import de.thm.mow2gamecollection.controller.helper.storage.UserSettings
import de.thm.mow2gamecollection.databinding.ActivityFirstRunBinding
import de.thm.mow2gamecollection.wordle.controller.LetterGridFragment
import de.thm.mow2gamecollection.wordle.model.grid.LetterStatus

class FirstRunActivity : KeyboardActivity() {
    private lateinit var model: FirstRunModel
    private lateinit var binding: ActivityFirstRunBinding
    private var playerName = ""
    private val nameInput: LetterGridFragment by lazy { binding.nameInput.getFragment() }
    private var index = 0
    private val maxUserNameLength: Int by lazy {
        resources.getInteger(R.integer.max_user_name_length)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFirstRunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        LetterGridFragment.newInstance(1, maxUserNameLength).let{
            supportFragmentManager.beginTransaction()
                .add(R.id.nameInput, it)
                .commit()
        }

        binding.startButton.setOnClickListener{
            if (playerName.isNotEmpty()) {
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
        model = FirstRunModel(this)
    }

    override fun addLetter(char: Char) {
        if (index < maxUserNameLength) {
            nameInput.updateTile(0, index, char, LetterStatus.UNKNOWN)
            playerName += char

            index++
        }
    }

    override fun removeLetter() {
        if (index > 0) {
            nameInput.removeLetter(0, --index)
            playerName = playerName.dropLast(1)
        }
    }

    override fun submit() {
        // TODO
    }
}

