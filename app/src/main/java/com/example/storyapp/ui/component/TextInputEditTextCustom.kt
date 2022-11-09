package com.example.storyapp.ui.component

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class TextInputEditTextCustom: TextInputLayout {
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

    private fun setupInputTextWatched() {
        textInputEditText.addTextChangedListener(onTextChanged = { _, _, _, _ ->
            isInputValid()
            doValidationInput()
        })
    }

    fun getInputValue(): String {
        return textInputEditText.text.toString()
    }

    fun isInputValid(): Boolean {
        val inputValue: String = getInputValue().trim()
        if (inputValue.isEmpty()) {
            return false
        }
        return true
    }

    fun doValidationInput() {
        if (isInputValid()) {
            this.error = ""
        } else {
            this.error = "value is not valid"
        }
    }

    private fun init() {
        textInputEditText = TextInputEditText(context)
        textInputEditText.inputType = InputType.TYPE_CLASS_TEXT
        setupInputTextWatched()

        this.addView(textInputEditText)
    }
}