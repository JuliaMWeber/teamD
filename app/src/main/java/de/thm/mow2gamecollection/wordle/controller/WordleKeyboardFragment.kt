package de.thm.mow2gamecollection.wordle.controller

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.findFragment
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.wordle.model.grid.LetterStatus

private const val TAG = "WordleKeyboardFragment"

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WordleKeyboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WordleKeyboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var keyboardLayout: String? = null
    private var param2: String? = null

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
            keyboardLayout = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        } ?: run {
            keyboardLayout = KeyboardLayout.QWERTZ.toString()
        }

        Log.d(TAG, "keyboard layout: $keyboardLayout")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wordle_keyboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        keyboardLayouts[selectedKeyboardLayout]?.let {
            for (keyboardRow in 0 until it.size) {
                for (keyLabel in it[keyboardRow]) {
                    val button = Button(context)
                    button.text = keyLabel.toString()
                    button.layoutParams = LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT,
                        1F
                    )
                    button.setPadding(4, 4, 4, 4)
                    context?.let {
                        button.backgroundTintList = ContextCompat.getColorStateList(it, R.color.wordle_unknown_panel_background)
                        button.setTextColor(ContextCompat.getColor(it, R.color.white))
                    }
                    button.id = View.generateViewId()
                    keyIDs.set(keyLabel, button.id)
                    button.setOnClickListener { handleButtonClick(button) }
                    val rowLayout = (getView() as LinearLayout).getChildAt(keyboardRow) as LinearLayout
                    rowLayout.addView(button)
                }
            }
        }
    }

    private fun handleButtonClick(button: Button) {
        (activity as WordleActivity).handleKeyboardClick(button)
    }

    fun updateButton(char: Char, letterStatus: LetterStatus) {
        keyIDs[char]?.let {
            val keyView = requireView().findViewById<Button>(it)
            context?.let {
                when (letterStatus) {
                    LetterStatus.UNKNOWN -> keyView.backgroundTintList = ContextCompat.getColorStateList(it, R.color.wordle_unknown_panel_background)
                    LetterStatus.CORRECT -> keyView.backgroundTintList = ContextCompat.getColorStateList(it, R.color.wordle_correct_panel_background)
                    LetterStatus.WRONG_POSITION -> keyView.backgroundTintList = ContextCompat.getColorStateList(it, R.color.wordle_wrong_position_panel_background)
                    LetterStatus.WRONG -> keyView.backgroundTintList = ContextCompat.getColorStateList(it, R.color.wordle_wrong_panel_background)
                    else -> return
                }
            }
        }
    }

    fun resetKeyboard() {
        keyboardLayouts[selectedKeyboardLayout]?.let {
            for (keyboardRow in 0 until it.size) {
                for (keyLabel in it[keyboardRow]) {
                    updateButton(keyLabel, LetterStatus.UNKNOWN)
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WordleKeyboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WordleKeyboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    enum class KeyboardLayout {
        QWERTZ,
        QWERTY
    }
}