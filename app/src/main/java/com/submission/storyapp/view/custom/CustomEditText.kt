package com.submission.storyapp.view.custom

import androidx.appcompat.widget.AppCompatEditText
import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import com.submission.storyapp.R

class CustomEditText : AppCompatEditText {

    private lateinit var lockIcon: Drawable
    private var isPasswordVisible = false

    constructor(context: Context) : super(context) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initialize()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }


    private fun togglePassVisibility() {
        isPasswordVisible = !isPasswordVisible
        inputType = if (isPasswordVisible) {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        setSelection(text!!.length)
        setCompoundDrawablesWithIntrinsicBounds(null, null, lockIcon, null)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initialize() {
        lockIcon = ContextCompat.getDrawable(context, R.drawable.ic_lock) as Drawable
        compoundDrawablePadding = 12
        setCompoundDrawablesWithIntrinsicBounds(null, null, lockIcon, null)
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableRight = 2
                if (event.rawX >= (right - compoundDrawables[drawableRight].bounds.width())) {
                    togglePassVisibility()
                    return@setOnTouchListener true
                }
            }
            false
        }

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (charSequence.isNullOrEmpty() || charSequence.length < 8) {
                    error = context.getString(R.string.invalid_password)
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(editable: Editable?) {}
        })
    }

}