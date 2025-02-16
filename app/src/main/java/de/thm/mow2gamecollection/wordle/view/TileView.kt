package de.thm.mow2gamecollection.wordle.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import androidx.core.view.updatePadding
import androidx.core.widget.TextViewCompat

class TileView : androidx.appcompat.widget.AppCompatTextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        gravity = Gravity.CENTER
        isAllCaps = true

        // shift letter using padding to center it properly
        updatePadding(SHIFT_TO_RIGHT, SHIFT_DOWN, 0, 0)

        // automatically scale text size (API >25)
        // TODO: solution for API <=25?!
        TextViewCompat.setAutoSizeTextTypeWithDefaults(this,  TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM)
    }

    companion object {
        const val SHIFT_TO_RIGHT = 25
        const val SHIFT_DOWN = 50
    }
}