package com.cashflowtracker.miranda.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import java.util.UUID

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT userId FROM user WHERE email = :email")
    suspend fun getUserIdByEmail(email: String): Int

    @Query("SELECT * FROM user WHERE userId = :email")
    suspend fun getByEmail(email: String): User

    @Query("SELECT * FROM user WHERE userId = :userId")
    suspend fun getByUserId(userId: UUID): User

    @Query("UPDATE user SET password = :newPassword WHERE userId = :userId")
    suspend fun updatePassword(userId: Int, newPassword: String)

    @Query("UPDATE user SET email = :newEmail WHERE userId = :userId")
    suspend fun updateEmail(userId: Int, newEmail: String)
}

@Dao
interface TransactionDao {
    @Upsert
    suspend fun insert(transaction: TransactionLog)

    @Delete
    suspend fun delete(transaction: TransactionLog)

    @Query("SELECT * FROM transactionlog WHERE userId = :userId ORDER BY dateTime DESC")
    suspend fun getAllByUserId(userId: UUID): List<TransactionLog>

    @Query("SELECT * FROM transactionlog WHERE transactionId = :transactionId")
    suspend fun getByTransactionId(transactionId: UUID): TransactionLog
}

@Dao
interface RecurrenceDao {
    @Upsert
    suspend fun insert(recurrence: Recurrence)

    @Delete
    suspend fun delete(recurrence: Recurrence)

    @Query("SELECT * FROM recurrence WHERE userId = :userId ORDER BY reoccursOn ASC")
    suspend fun getByUserId(userId: UUID): List<Recurrence>

    @Query("SELECT * FROM recurrence WHERE recurrenceId = :recurrenceId")
    suspend fun getByRecurrenceId(recurrenceId: UUID): Recurrence
}

@Dao
interface AccountDao {
    @Upsert
    suspend fun insert(account: Account)

    @Delete
    suspend fun delete(account: Account)

    @Query("SELECT * FROM account WHERE userId = :userId ORDER BY balance DESC")
    suspend fun getByUserId(userId: UUID): List<Account>
}

@Dao
interface CustomCategoryDao {
    @Upsert
    suspend fun insert(customCategory: CustomCategory)

    @Delete
    suspend fun delete(customCategory: CustomCategory)

    @Query("SELECT * FROM customcategory WHERE userId = :userId GROUP BY type")
    suspend fun getByUserId(userId: UUID): List<CustomCategory>
}

@Dao
interface AchievementDao {
    @Upsert
    suspend fun insert(achievement: Achievement)

    @Delete
    suspend fun delete(achievement: Achievement)

    @Query("SELECT * FROM achievement WHERE userId = :userId GROUP BY name ORDER BY achievedOn DESC, level DESC")
    suspend fun getByUserId(userId: UUID): List<Achievement>
}