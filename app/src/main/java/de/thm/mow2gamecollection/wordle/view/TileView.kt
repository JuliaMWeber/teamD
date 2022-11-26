package de.thm.mow2gamecollection.wordle.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import androidx.core.widget.TextViewCompat
import de.thm.mow2gamecollection.R
import de.thm.mow2gamecollection.wordle.model.grid.LetterStatus

class TileView : androidx.appcompat.widget.AppCompatTextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        gravity = Gravity.CENTER
        isAllCaps = true

        // shift letter using padding to center it properly
        updatePadding(25, 50, 0, 0)

        // automatically scale text size (API >25)
        // TODO: solution for API <=25?!
        TextViewCompat.setAutoSizeTextTypeWithDefaults(this,  TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM)
    }

    fun update(status: LetterStatus, letter: Char? = null) {
        when (status) {
            LetterStatus.BLANK -> {
                setBackgroundColor(ContextCompat.getColor(context, R.color.wordle_empty_panel_background))
                setTextColor(ContextCompat.getColor(context, R.color.white))
                setShadowLayer(10F, 0F, 0F, ContextCompat.getColor(context, R.color.black))
                text = ""
                return
            }
            LetterStatus.UNKNOWN -> {
                setBackgroundColor(ContextCompat.getColor(context, R.color.wordle_unknown_panel_background))
                setTextColor(ContextCompat.getColor(context, R.color.white))
                setShadowLayer(10F, 0F, 0F, ContextCompat.getColor(context, R.color.black))
            }
            LetterStatus.CORRECT -> {
                setBackgroundColor(ContextCompat.getColor(context, R.color.wordle_correct_panel_background))
                setTextColor(ContextCompat.getColor(context, R.color.white))
                setShadowLayer(10F, 0F, 0F, ContextCompat.getColor(context, R.color.black))
            }
            LetterStatus.WRONG_POSITION -> {
                setBackgroundColor(ContextCompat.getColor(context, R.color.wordle_wrong_position_panel_background))
                setTextColor(ContextCompat.getColor(context, R.color.white))
                setShadowLayer(10F, 0F, 0F, ContextCompat.getColor(context, R.color.black))
            }
            LetterStatus.WRONG -> {
                setBackgroundColor(ContextCompat.getColor(context, R.color.wordle_wrong_panel_background))
                setTextColor(ContextCompat.getColor(context, R.color.white))
                setShadowLayer(0F, 0F, 0F, ContextCompat.getColor(context, R.color.wordle_wrong_panel_text))
            }
        }
        letter?.let { text = it.toString() }
    }
}