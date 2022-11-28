package de.thm.mow2gamecollection.controller

import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity

abstract class KeyboardActivity : AppCompatActivity() {
    abstract fun addLetter(char: Char)

    abstract fun removeLetter()

    abstract fun submit()

    // handle physical keyboard input
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            in 29..54 -> {
                event?.let {
                    addLetter(event.unicodeChar.toChar())
                }
            }
            KeyEvent.KEYCODE_DEL -> {
                removeLetter()
            }
            else -> {
                super.onKeyUp(keyCode, event)
            }
        }
        return true
    }
}