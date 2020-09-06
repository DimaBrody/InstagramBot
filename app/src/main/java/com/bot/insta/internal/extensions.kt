package com.bot.insta.internal

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView

fun <E> E.setOnTextWatcher(onTextChanged: (CharSequence?) -> Unit) where E : EditText {
    addTextChangedListener(object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged(s)
        }

    })
}

fun <T,G> T.setMultipleTextWatcher(vararg views: G) where G : EditText, T : TextView {
    views.forEach {
        it.setOnTextWatcher {
            isEnabled = views.all { content ->  !content.text.isNullOrEmpty() }
        }
    }

}
