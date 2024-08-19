package com.cashflowtracker.miranda

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cashflowtracker.miranda.utils.validateEmail
import com.cashflowtracker.miranda.utils.validatePassword
import org.junit.Test
import org.junit.Assert.*

class TestStringValidator {
    @Test
    fun testValidateEmail() {
        assertTrue(validateEmail("a@a.com"))
        assertFalse(validateEmail("a"))
        assertFalse(validateEmail("a@a"))
        assertFalse(validateEmail("a@.com"))
        assertTrue(validateEmail("CUSTOM_e-mail+address@provider.com"))
        assertTrue(validateEmail("email@long.provider.com"))
        assertTrue(validateEmail("email@very.long.provider.com"))
        assertTrue(validateEmail("email@dashed-provider.com"))
        assertFalse(validateEmail("invalid#email@format.com"))
    }

    @Test
    fun testValidatePassword() {
        assertFalse(validatePassword("short"))
        assertTrue(validatePassword("password"))
        assertTrue(validatePassword("12345678"))
        assertTrue(validatePassword("LoNg&Pr3tty\$tr0ngPa55w0rd."))
    }
}
