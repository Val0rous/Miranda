package com.cashflowtracker.miranda.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface UsersDao {
    @Insert
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT userId FROM user WHERE email = :email")
    fun getUserIdByEmail(email: String): UUID

    @Query("SELECT * FROM user WHERE userId = :email")
    fun getByEmail(email: String): User

    @Query("SELECT * FROM user WHERE userId = :userId")
    fun getByUserId(userId: UUID): User

    @Query("UPDATE user SET password = :newPassword WHERE userId = :userId")
    suspend fun updatePassword(userId: UUID, newPassword: String)

    @Query("UPDATE user SET email = :newEmail WHERE userId = :userId")
    suspend fun updateEmail(userId: UUID, newEmail: String)

    @Query("SELECT * FROM user")
    fun listAll(): Flow<List<User>>
}

@Dao
interface TransactionsDao {
    @Upsert
    suspend fun upsert(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)

    @Query("SELECT * FROM [transaction] WHERE userId = :userId ORDER BY dateTime DESC")
    fun getAllByUserId(userId: UUID): List<Transaction>

    @Query("SELECT * FROM [transaction] WHERE transactionId = :transactionId")
    fun getByTransactionId(transactionId: UUID): Transaction
}

@Dao
interface RecurrencesDao {
    @Upsert
    suspend fun upsert(recurrence: Recurrence)

    @Delete
    suspend fun delete(recurrence: Recurrence)

    @Query("SELECT * FROM recurrence WHERE userId = :userId ORDER BY reoccursOn ASC")
    fun getByUserId(userId: UUID): List<Recurrence>

    @Query("SELECT * FROM recurrence WHERE recurrenceId = :recurrenceId")
    fun getByRecurrenceId(recurrenceId: UUID): Recurrence
}

@Dao
interface AccountsDao {
    @Upsert
    suspend fun upsert(account: Account)

    @Delete
    suspend fun delete(account: Account)

    @Query("SELECT * FROM account WHERE userId = :userId ORDER BY balance DESC")
    fun getByUserId(userId: UUID): List<Account>
}

@Dao
interface CustomCategoriesDao {
    @Upsert
    suspend fun upsert(customCategory: CustomCategory)

    @Delete
    suspend fun delete(customCategory: CustomCategory)

    @Query("SELECT * FROM customcategory WHERE userId = :userId GROUP BY type")
    fun getByUserId(userId: UUID): List<CustomCategory>
}

@Dao
interface AchievementsDao {
    @Upsert
    suspend fun upsert(achievement: Achievement)

    @Delete
    suspend fun delete(achievement: Achievement)

    @Query("SELECT * FROM achievement WHERE userId = :userId GROUP BY name ORDER BY achievedOn DESC, level DESC")
    fun getByUserId(userId: UUID): List<Achievement>
}