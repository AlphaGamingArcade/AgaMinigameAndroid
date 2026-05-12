package com.alphagamingarcade.core.extensions

import android.util.Patterns
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Checks if the string is a valid email address.
 *
 * @return `true` if the string is a valid email address, `false` otherwise.
 */
fun String?.isEmailValid(): Boolean {
    return !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

/**
 * Checks if the string is a valid password based on the specified criteria.
 *
 * @return `true` if the string is a valid password, `false` otherwise.
 */
fun String?.isPasswordValid(): Boolean {
//    val passwordRegex = "^(?=.*\\d)(?=.*[a-z]).{8,20}$"
    val passwordRegex = "^.{8,}$"
    val pattern: Pattern = Pattern.compile(passwordRegex)
    val matcher: Matcher = pattern.matcher(this ?: "")
    return matcher.matches()
}

fun String?.isNicknameValid(): Boolean {
    return isNicknameLengthValid() && isNicknameFormatValid()
}

fun String?.isNicknameLengthValid(): Boolean {
    return !isNullOrBlank() && length in 3..64
}

fun String?.isNicknameFormatValid(): Boolean {
    if (this.isNullOrBlank()) return false
    val regex = Regex("^[A-Za-z0-9 _.-]+$")
    return regex.matches(this)
}


fun String?.isAccountLengthValid(): Boolean {
    return !isNullOrEmpty() && length in 3..64
}

fun String?.isAccountFormatValid(): Boolean {
    if (this.isNullOrEmpty()) return false
    val regex = Regex("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$")
    return regex.matches(this)
}