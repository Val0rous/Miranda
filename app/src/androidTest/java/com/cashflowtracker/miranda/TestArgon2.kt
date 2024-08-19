package com.cashflowtracker.miranda

import com.cashflowtracker.miranda.utils.generateSalt
import com.cashflowtracker.miranda.utils.hashPassword
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class TestArgon2 {
    @Test
    fun testHash() {
        val salt = generateSalt()
        val password = "Password@123"
        val hash = hashPassword(password, salt)
        assertEquals(hash, hashPassword(password, salt))
        assertNotEquals(hash, hashPassword("Password@1234", salt))
        var anotherSalt = ""
        do {
            anotherSalt = generateSalt()
        } while (anotherSalt == salt)
        assertNotEquals(hash, hashPassword("Password@123", anotherSalt))
    }
}