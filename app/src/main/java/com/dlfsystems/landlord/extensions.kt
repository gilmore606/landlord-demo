package com.dlfsystems.landlord

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.service.autofill.Validators.and
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.Executors

operator fun CompositeDisposable.plusAssign(subscription: Disposable) {
    add(subscription)
}

fun prefs(context: Context) : SharedPreferences {
    return PreferenceManager.getDefaultSharedPreferences(context)
}

private val IO_EXECUTOR = Executors.newSingleThreadExecutor()

fun ioThread(f : () -> Unit) {
    IO_EXECUTOR.execute(f)
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(if (currentFocus == null) View(this) else currentFocus)
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
    })
}

fun EditText.validate(validator: (String) -> Boolean, message: String, saver: ((String) -> Unit)? = null) {
    this.afterTextChanged {
        if (!validator(it)) this.error = message
        else {
            this.error = null
            if (saver != null) saver(it)
        }
    }
    this.error = if (validator(this.text.toString())) null else message
}

fun EditText.validateNumeric(validator: (String) -> Boolean, message: String, saver: ((String) -> Unit)? = null) {
    this.afterTextChanged {
        if (it == "0") text.clear()
        if (!validator(it)) this.error = message
        else {
            this.error = null
            if (saver != null) saver(it)
        }
    }
    this.error = if (validator(this.text.toString())) null else message
}

fun String.isValidCoord(): Boolean {
    if (length < 1) return false
    if (toDouble() == 0.0) return false
    if (indexOf('.') < 0) return false
    return true
}

fun EditText.setIfChanged(newText: String) {
    if ((text.toString() != newText) and (text.toString() != (newText + " ")))
        setText(newText)
}

fun TextView.setIfChanged(newText: String) {
    if ((text.toString() != newText) and (text.toString() != (newText + " ")))
        setText(newText)
}

fun Spinner.setIfChanged(i: Int) {
    if (selectedItemPosition != i) {
        setSelection(i)
    }
}
