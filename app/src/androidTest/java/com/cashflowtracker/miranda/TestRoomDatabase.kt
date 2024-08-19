package com.cashflowtracker.miranda

import android.content.Context
import androidx.media3.test.utils.TestUtil
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.cashflowtracker.miranda.utils.generateSalt
import com.cashflowtracker.miranda.utils.hashPassword
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.cashflowtracker.miranda.data.database.MirandaDatabase
import com.cashflowtracker.miranda.data.database.User
import com.cashflowtracker.miranda.data.database.UsersDao
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Before
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TestRoomDatabase {
    private lateinit var db: MirandaDatabase

    @Before
    fun createDatabase() {
        //val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        //assertEquals("com.cashflowtracker.miranda", appContext.packageName)
        db = Room.inMemoryDatabaseBuilder(
            appContext, MirandaDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    suspend fun testUsers() {
        val salt = generateSalt()
        val user = User(
            name = "John Doe",
            email = "john.c.breckinridge@altostrat.com",
            salt = salt,
            password = hashPassword("password", salt),
            country = "",
            currency = "",
            encryptionKey = ""
        )
        db.usersDao().insert(user)
        assertEquals(user, db.usersDao().getByEmail(user.email))
    }
}