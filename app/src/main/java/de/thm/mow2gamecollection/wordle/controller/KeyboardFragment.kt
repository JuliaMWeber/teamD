package de.thm.mow2gamecollection.wordle.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.controller.KeyboardActivity
import de.thm.mow2gamecollection.wordle.model.grid.LetterStatus

/**
 * A simple [Fragment] subclass.
 * Use the [WordleKeyboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WordleKeyboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var keyboardLayout: KeyboardLayout? = KeyboardLayout.QWERTZ
    private var swapFunctionButtons: Boolean? = false
    private var keyStateMap = mutableMapOf<Char, LetterStatus>()

    private val keyboardLayouts: HashMap<KeyboardLayout, Array<CharArray>> = hashMapOf(
        KeyboardLayout.QWERTZ to arrayOf(
            charArrayOf('q', 'w', 'e', 'r', 't', 'z', 'u', 'i', 'o', 'p'),
            charArrayOf('a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l'),
            charArrayOf('←', 'y', 'x', 'c', 'v', 'b', 'n', 'm', '✓')
        ),
        KeyboardLayout.QWERTY to arrayOf(
            charArrayOf('q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p'),
            charArrayOf('a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l'),
            charArrayOf('←', 'z', 'x', 'c', 'v', 'b', 'n', 'm', '✓')
        )
    )

    private val selectedKeyboardLayout = KeyboardLayout.QWERTZ

    private val keyIDs = HashMap<Char, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            keyboardLayout = KeyboardLayout.valueOf(it.getString(ARG_KEYBOARD_LAYOUT)!!)
            swapFunctionButtons = it.getBoolean(ARG_SWAP_FUNCTION_BUTTONS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wordle_keyboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedInstanceState?.let {
            val keys = it.getCharArray(KEYBOARD_STATE_KEYS_KEY)
            val values = it.getStringArray(KEYBOARD_STATE_VALUES_KEY)
            for (i in 0 until keys!!.size) {
                keyStateMap.put(keys[i], LetterStatus.valueOf(values!![i]))
            }
        }

        keyboardLayouts[selectedKeyboardLayout]?.let {
            for (row in it.indices) {
                val rowLayout = (getView() as LinearLayout).getChildAt(row) as LinearLayout
                for (keyLabel in it[row]) {

                    val button = Button(
                        ContextThemeWrapper(context, R.style.keyboardButton),
                        null,
                        0
                    ).apply {
                        text = keyLabel.toString()
                        layoutParams = LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.MATCH_PARENT,
                            1F
                        )
                    }
                    button.id = View.generateViewId()
                    keyIDs[keyLabel] = button.id
                    button.setOnClickListener { handleButtonClick(button) }
                    rowLayout.addView(button)
                    keyStateMap.get(keyLabel)?.let {
                        updateButton(keyLabel, it)
                    }
                }
            }
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val keyStateMapValues = mutableListOf<String>()
        for (value in keyStateMap.values) {
            keyStateMapValues.add(value.toString())
        }
        outState.putCharArray(KEYBOARD_STATE_KEYS_KEY, keyStateMap.keys.toCharArray())
        outState.putStringArray(KEYBOARD_STATE_VALUES_KEY, keyStateMapValues.toTypedArray())
    }

    private fun handleButtonClick(button: Button) {
        val activity = activity as KeyboardActivity
        when (button.text) {
            "✓" -> {
                activity.submit()
            }
            "←" -> {
                activity.removeLetter()
            }
            else -> {
                val char = button.text.first()
                activity.addLetter(char)
            }
        }
    }

    fun updateButton(char: Char, letterStatus: LetterStatus) {
        keyIDs[char]?.let {
            // update keyStateMap
            keyStateMap.put(char, letterStatus)
            val keyView = requireView().findViewById<Button>(it)
            context?.let {
                keyView.setTextColor(ContextCompat.getColor(it, R.color.white))
                when (letterStatus) {
                    LetterStatus.UNKNOWN -> {
                        keyView.setBackgroundResource(R.drawable.pixel_button_small_square_light_grey)
                        keyView.setTextAppearance(R.style.keyboardLabelDark)
                    }
                    LetterStatus.CORRECT -> {
                        keyView.setBackgroundResource(R.drawable.pixel_button_small_square_thm_primary)
                        keyView.setTextAppearance(R.style.keyboardLabelLight)
                    }
                    LetterStatus.WRONG_POSITION -> {
                        keyView.setBackgroundResource(R.drawable.pixel_button_small_square_thm_orange)
                        keyView.setTextAppearance(R.style.keyboardLabelLight)
                    }
                    LetterStatus.WRONG -> {
                        keyView.setBackgroundResource(R.drawable.pixel_button_small_square_dark_grey)
                        keyView.setTextAppearance(R.style.keyboardLabelGrey)
                    }
                    else -> return
                }
            }
        }
    }

    fun resetKeyboard() {
        keyboardLayouts[selectedKeyboardLayout]?.let {
            for (keyboardRow in it) {
                for (keyLabel in keyboardRow) {
                    updateButton(keyLabel, LetterStatus.UNKNOWN)
                }
            }
        }
    }

    companion object {
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_KEYBOARD_LAYOUT = "keyboardLayout"
        private const val ARG_SWAP_FUNCTION_BUTTONS = "swapFunctionButtons"

        private const val KEYBOARD_STATE_KEYS_KEY = "keyboardStateKeys"
        private const val KEYBOARD_STATE_VALUES_KEY = "keyboardStateValues"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param keyboardLayout Parameter 1.
         * @param swapFunctionButtons Parameter 2.
         * @return A new instance of fragment WordleKeyboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(keyboardLayout: KeyboardLayout, swapFunctionButtons: Boolean) =
            WordleKeyboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_KEYBOARD_LAYOUT, keyboardLayout.toString())
                    putBoolean(ARG_SWAP_FUNCTION_BUTTONS, swapFunctionButtons)
                }
            }
    }

    enum class KeyboardLayout {
        QWERTZ,
        QWERTY
    }
}
