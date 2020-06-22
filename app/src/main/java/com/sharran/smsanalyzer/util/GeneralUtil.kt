package com.sharran.smsanalyzer.util

import android.util.Patterns
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by SHARRAN on 21/6/20.
 */

fun Date.toStringFormat(format: String): String {
    val formatter = SimpleDateFormat(format);
    return formatter.format(this)
}

fun isValidMobile(phone: String): Boolean {
    return "^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}\$".toPattern().matcher(phone).matches()
}