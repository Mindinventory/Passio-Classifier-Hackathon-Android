package com.mindinventory.dermatai.common.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible

fun View.hide() {
    this.isVisible = false
}

fun View.show() {
    this.isVisible = true
}

fun show(vararg view: View) {
    view.iterator().forEach {
        it.show()
    }
}

fun hide(vararg view: View) {
    view.iterator().forEach {
        it.hide()
    }
}

fun View.hideKeyboard() {
    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

