package de.thm.mow2gamecollection.wordle.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import de.thm.mow2gamecollection.wordle.helper.MAX_TRIES
import de.thm.mow2gamecollection.wordle.helper.SaveGameHelper
import de.thm.mow2gamecollection.wordle.helper.WORD_LENGTH
import de.thm.mow2gamecollection.wordle.model.WordleModel
import de.thm.mow2gamecollection.wordle.model.WordleViewModel
import de.thm.mow2gamecollection.wordle.model.game.WordleGame
import de.thm.mow2gamecollection.wordle.model.grid.LetterStatus

// Debugging
private const val DEBUG = true
private const val TAG = "WordleViewModel"

class WordleActivityViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application), WordleViewModel {

    var tries: Int = 0
        private set

    // Array of the user's guesses
    private val _userGuesses : MutableLiveData<MutableList<String>> = MutableLiveData()
    val userGuesses : LiveData<MutableList<String>> = _userGuesses

    // Array representing the letters in the grid
    private val _tileLetterArray : MutableLiveData<Array<Array<Char>>> = MutableLiveData()
    val tileLetterArray : LiveData<Array<Array<Char>>> = _tileLetterArray

    // Array representing the correctness of the letters in the grid
    private val _tileStatusArray : MutableLiveData<Array<Array<LetterStatus>>> = MutableLiveData()
    val tileStatusArray : LiveData<Array<Array<LetterStatus>>> = _tileStatusArray

    private val _keyStateMap : MutableLiveData<MutableMap<Char, LetterStatus>> = MutableLiveData()
    val keyStateMap : LiveData<MutableMap<Char, LetterStatus>> = _keyStateMap

    var model: WordleModel

    private var _currentIndex : MutableLiveData<Int> = MutableLiveData()
    val currentIndex: LiveData<Int> = _currentIndex

    var userInput = ""
        private set

    init {
        Log.d(TAG, "ViewModel created")
        Log.d(TAG, "savedStateHandle: ${savedStateHandle}")
        model = WordleModel(this, SaveGameHelper.getInstance(application))

        _currentIndex.value = 0
        _tileLetterArray.value = Array(MAX_TRIES) { Array(WORD_LENGTH) { ' ' } }
        _tileStatusArray.value = Array(MAX_TRIES) { Array(WORD_LENGTH) { LetterStatus.BLANK } }
        _keyStateMap.value = HashMap<Char, LetterStatus>()

        model.init()
    }


    fun addLetter(letter: Char) : Boolean {
        Log.d(TAG, "addLetter at index ${_currentIndex.value}, \tuserInput: $userInput")
        if (userInput.length >= WORD_LENGTH) {
            return false
        }
        userInput += letter
        _tileLetterArray.value!![tries][_currentIndex.value!!] = letter
        Log.d(TAG, "\tletter $letter added at index ${_currentIndex.value}, \tuserInput: $userInput")
        _currentIndex.value = _currentIndex.value!! + 1
        return true
    }

    fun removeLetter() : Boolean {
        if (userInput.isNotEmpty()) {
            userInput = userInput.dropLast(1)
            _currentIndex.value = _currentIndex.value!! - 1
        }
        Log.d(TAG, "${_currentIndex.value}")
        return true
    }

    fun onSubmitGuess() {
        _currentIndex.value = 0

        if (tries == MAX_TRIES) {
            tries = 0
        } else {
            tries++
        }

        _userGuesses.value?.let {}
        val newList: MutableList<String> = _userGuesses.value ?: mutableListOf()
        newList.add(userInput)
        _userGuesses.value = newList
        userInput = ""
    }

    override fun initializeGrid(game: WordleGame) {
        for (guess in game.userGuesses) {
            // TODO
        }
    }

    override fun update(row: Int, column: Int, letter: Char, letterStatus: LetterStatus) {
        val newTileStatusArray: Array<Array<LetterStatus>> = _tileStatusArray.value!!
        newTileStatusArray[row][column] = letterStatus
        _tileStatusArray.value = newTileStatusArray

        val newTileLetterArray: Array<Array<Char>> = _tileLetterArray.value!!
        newTileLetterArray[row][column] = letter
        _tileLetterArray.value = newTileLetterArray
    }
}