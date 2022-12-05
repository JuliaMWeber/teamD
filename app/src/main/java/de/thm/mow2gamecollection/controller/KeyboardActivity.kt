package de.thm.mow2gamecollection.controller

import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity

private const val KEYCODE_A = 29
private const val KEYCODE_Z = 54

abstract class KeyboardActivity : AppCompatActivity() {
    abstract fun addLetter(char: Char)

    abstract fun removeLetter()

    abstract fun submit()

    // handle physical keyboard input
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            in KEYCODE_A..KEYCODE_Z -> {
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