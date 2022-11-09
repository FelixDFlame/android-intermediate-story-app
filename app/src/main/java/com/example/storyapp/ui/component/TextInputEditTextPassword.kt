package com.example.storyapp.ui.component

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class TextInputEditTextPassword : TextInputLayout {
    val MIN_LENGTH = 6
    lateinit var textInputEditText: TextInputEditText

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    fun setupInputTextChanged() {
        textInputEditText.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            isInputValid()
            doValidationInput()
        })
    }

    fun getInputValue(): String {
        return textInputEditText.text.toString()
    }

    fun isInputValid(): Boolean {
        val inputValue: String = getInputValue()
        if (inputValue.length < MIN_LENGTH) {
            return false
        }
        return true
    }

    fun doValidationInput() {
        if (isInputValid()) {
            this.error = ""
        } else {
            this.error = "min 6 characters"
        }
    }

    private fun init() {
        textInputEditText = TextInputEditText(context)
        textInputEditText.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        setupInputTextChanged()
        this.addView(textInputEditText)
    }


}