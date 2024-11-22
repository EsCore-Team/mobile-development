package com.dicoding.escore.view.validation

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewParent
import com.dicoding.escore.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class EmailValidation @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextInputEditText(context, attrs, defStyleAttr) {

    private var textInputLayout: TextInputLayout? = null
    private var isInitialized = false

    init {
        setupView()
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isInitialized) {
                    validateEmail(s)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupView() {
        isFocusable = true
        isFocusableInTouchMode = true
        isClickable = true
        isEnabled = true
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        gravity = Gravity.CENTER_VERTICAL
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
    }

    private fun validateEmail(email: CharSequence?) {
        if (textInputLayout == null) {
            textInputLayout = findParentTextInputLayout()
        }
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        if (email != null && !email.matches(emailPattern.toRegex())) {
            textInputLayout?.error = context.getString(R.string.email_validation)
        } else {
            textInputLayout?.error = null
        }
    }

    private fun findParentTextInputLayout(): TextInputLayout? {
        var parentView: ViewParent? = parent
        while (parentView != null) {
            if (parentView is TextInputLayout) {
                return parentView
            }
            parentView = (parentView as? ViewParent)?.parent
        }
        return null
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        isInitialized = true
    }
}
