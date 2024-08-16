package com.cashflowtracker.miranda.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
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
    @Insert
    suspend fun insert(transaction: TransactionLog)

    @Delete
    suspend fun delete(transaction: TransactionLog)

    @Update
    suspend fun update(transaction: TransactionLog)

    @Query("SELECT * FROM transactionlog WHERE userId = :userId ORDER BY dateTime DESC")
    suspend fun getAllByUserId(userId: UUID): List<TransactionLog>

    @Query("SELECT * FROM transactionlog WHERE transactionId = :transactionId")
    suspend fun getByTransactionId(transactionId: String): TransactionLog
}

@Dao
interface RecurrenceDao {
    @Insert
    suspend fun insert(recurrence: Recurrence)

    @Delete
    suspend fun delete(recurrence: Recurrence)

    @Update
    suspend fun update(recurrence: Recurrence)

    @Query("SELECT * FROM recurrence WHERE userId = :userId ORDER BY reoccursOn ASC")
    suspend fun getByUserId(userId: UUID): List<Recurrence>

    @Query("SELECT * FROM recurrence WHERE recurrenceId = :recurrenceId")
    suspend fun getByRecurrenceId(recurrenceId: String): Recurrence
}

@Dao
interface AccountDao {
    @Insert
    suspend fun insert(account: Account)

    @Delete
    suspend fun delete(account: Account)

    @Update
    suspend fun update(account: Account)

    @Query("SELECT * FROM account WHERE userId = :userId ORDER BY balance DESC")
    suspend fun getByUserId(userId: UUID): List<Account>
}

@Dao
interface CustomCategoryDao {
    @Insert
    suspend fun insert(customCategory: CustomCategory)

    @Delete
    suspend fun delete(customCategory: CustomCategory)

    @Update
    suspend fun update(customCategory: CustomCategory)

    @Query("SELECT * FROM customcategory WHERE userId = :userId GROUP BY type")
    suspend fun getByUserId(userId: UUID): List<CustomCategory>
}

@Dao
interface AchievementDao {
    @Insert
    suspend fun insert(achievement: Achievement)

    @Delete
    suspend fun delete(achievement: Achievement)

    @Update
    suspend fun update(achievement: Achievement)

    @Query("SELECT * FROM achievement WHERE userId = :userId ORDER BY achievedOn DESC")
    suspend fun getByUserId(userId: UUID): List<Achievement>

}