package de.thm.mow2gamecollection.wordle.helper

import android.content.Context
import android.util.Log
import androidx.preference.PreferenceManager
import de.thm.mow2gamecollection.wordle.model.game.WordleGame
import org.apache.commons.collections4.queue.CircularFifoQueue

// Debugging
private const val DEBUG = false
private const val TAG = "SaveGameHelper"

// keys for SharedPreferences
private const val TARGET_WORD_KEY = "targetWord"
private const val USER_GUESSES_KEY = "userGuesses"
private const val RECENT_TARGET_WORDS_KEY = "recentTargetWords"

/**
 * Singleton helper class to read from and save to Shared Preferences
 */
class SaveGameHelper private constructor(context: Context) {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    val recentTargetWords = CircularFifoQueue<String>(NUMBER_OF_RECENT_TARGET_WORDS)
    var currentGame: WordleGame? = null

    init {
        recentTargetWords

                // get recent target words
        preferences.getString(RECENT_TARGET_WORDS_KEY, null)?.let { retrievedRecentTargetWords ->
            if (DEBUG) Log.d(TAG, "recent target words retrieved: $retrievedRecentTargetWords")
            retrievedRecentTargetWords.split(", ").forEach {
                recentTargetWords.add(it)
            }
        } ?: run { if (DEBUG) Log.d(TAG, "no recent target words saved") }
    }


    // save game progress to SharedPreferences
    fun saveGameState() {
        // TODO
        if (DEBUG) Log.d(TAG, "saveGameState")

        val editor = preferences.edit()


        currentGame?.let {
            if (it.hasEnded) {
                // remove saved game state from Shared Preferences (if existent)
                editor.remove(TARGET_WORD_KEY)
                editor.remove(USER_GUESSES_KEY)
            } else {
                editor.putString(TARGET_WORD_KEY, it.targetWord)
                editor.putString(USER_GUESSES_KEY, it.userGuesses.joinToString())
            }
        } ?: run {
            if (DEBUG) Log.d(TAG, "currentGame is null!")
        }


        editor.putString(RECENT_TARGET_WORDS_KEY, recentTargetWords.joinToString())
        // Apply to storage
        editor.apply()
    }

    // retrieve game progress from SharedPreferences
    fun retrieveSaveGame() : WordleGame? {
        if (DEBUG) Log.d(TAG, "retrieveSaveGame")

        var targetWord : String? = null
        val userGuesses = mutableListOf<String>()

        // get target word
        preferences.getString(TARGET_WORD_KEY, null)?.let {
            targetWord = it
            if (DEBUG) Log.d(TAG, "targetWord retrieved: $it")
        } ?: run { if (DEBUG) Log.d(TAG, "no targetWord saved") }

        // get user's guesses
        preferences.getString(USER_GUESSES_KEY, null)?.let { retrievedGuesses ->
            if (DEBUG) Log.d(TAG, "user's guesses retrieved: $retrievedGuesses")
            retrievedGuesses.split(", ").forEach {
                userGuesses.add(it)
            }
        } ?: run { if (DEBUG) Log.d(TAG, "no user guesses saved") }

//        return when(targetWord) {
//            null -> null
//            else -> WordleGame(targetWord!!, userGuesses)
//        }
        return WordleGame("trial", mutableListOf("brown", "trays"))
    }

    fun deleteSaveGame() {
        // TODO
    }

    fun wordRecentlyBeenPlayed(word: String) : Boolean {
        return recentTargetWords.contains(word)
    }

    fun addToRecentTargetWords(word: String) {
        // TODO
    }


    companion object {
        private var instance : SaveGameHelper? = null
        fun getInstance(context: Context) : SaveGameHelper {
            if (instance == null) {
                instance = SaveGameHelper(context)
            }
            return instance!!
        }
    }
}