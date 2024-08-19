package com.cashflowtracker.miranda.utils

import com.lambdapioneer.argon2kt.Argon2Kt
import com.lambdapioneer.argon2kt.Argon2Mode


/** Generate a 32-character alphanumeric salt */
fun generateSalt(): String {
    val allowedChars = ('0'..'9') + ('a'..'z') + ('A'..'Z')
    return List(32) { allowedChars.random() }.joinToString("")
}

/** Concatenates password and salt, then calculates 256-bit hash using Argon2
 * @param password Password to hash
 * @param salt Salt to use
 * @return Hashed password as a hexadecimal string */
fun hashPassword(password: String, salt: String): String {
    val saltedPassword = password + salt
    val argon2 = Argon2Kt()
    return argon2.hash(
        mode = Argon2Mode.ARGON2_I,
        password = saltedPassword.toByteArray(),
        salt = salt.toByteArray(),
        tCostInIterations = 5,
        mCostInKibibyte = 65536
    ).rawHashAsHexadecimal()
}