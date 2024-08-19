package com.cashflowtracker.miranda.utils

/** Validates an email address
 * @param email The email address to validate
 * @return True if the email address is valid, false otherwise */
fun validateEmail(email: String): Boolean {
    val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")
    return emailRegex.matches(email)
}

/** Validates a password
 * @param password The password to validate
 * @return True if the password is valid, false otherwise */
fun validatePassword(password: String): Boolean {
    val passwordRegex = Regex("^.{8,128}\$")
    return passwordRegex.matches(password)
}