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

    @Query("SELECT * FROM user WHERE email = :email")
    fun getByEmailOrNull(email: String): User?

    @Query("SELECT * FROM user WHERE email = :email")
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

    @Query("DELETE FROM 'transaction' WHERE transactionId = :transactionId")
    suspend fun deleteByTransactionId(transactionId: UUID)

    @Query("SELECT * FROM 'transaction' WHERE userId = :userId ORDER BY createdOn DESC")
    fun getAllByUserIdFlow(userId: UUID): Flow<List<Transaction>>

    @Query("SELECT * FROM 'transaction' WHERE userId = :userId AND location IS NOT NULL AND location <> '' ORDER BY createdOn DESC")
    fun getAllWithLocationByUserIdFlow(userId: UUID): Flow<List<Transaction>>

    @Query("SELECT * FROM 'transaction' WHERE transactionId = :transactionId")
    fun getByTransactionId(transactionId: UUID): Transaction

    @Query("SELECT * FROM 'transaction' WHERE transactionId = :transactionId")
    fun getByTransactionIdFlow(transactionId: UUID): Flow<Transaction>

    @Query("SELECT * FROM 'transaction' WHERE source = :accountId OR destination = :accountId ORDER BY createdOn DESC")
    fun getAllByAccountIdFlow(accountId: String): Flow<List<Transaction>>
}

@Dao
interface RecurrencesDao {
    @Upsert
    suspend fun upsert(recurrence: Recurrence)

    @Delete
    suspend fun delete(recurrence: Recurrence)

    @Query("DELETE FROM recurrence WHERE recurrenceId = :recurrenceId")
    suspend fun deleteByRecurrenceId(recurrenceId: UUID)

    @Query("SELECT * FROM recurrence WHERE userId = :userId ORDER BY reoccursOn ASC")
    fun getAllByUserIdFlow(userId: UUID): Flow<List<Recurrence>>

    @Query("SELECT * FROM recurrence WHERE recurrenceId = :recurrenceId")
    fun getByRecurrenceId(recurrenceId: UUID): Recurrence

    @Query("SELECT * FROM recurrence WHERE recurrenceId = :recurrenceId")
    fun getByRecurrenceIdFlow(recurrenceId: UUID): Flow<Recurrence>

    @Query("SELECT * FROM recurrence WHERE source = :accountId OR destination = :accountId ORDER BY reoccursOn ASC")
    fun getAllByAccountIdFlow(accountId: String): Flow<List<Recurrence>>
}

@Dao
interface NotificationsDao {
    @Upsert
    suspend fun upsert(notification: Notification)

    @Delete
    suspend fun delete(notification: Notification)

    @Query("SELECT * FROM notification WHERE userId = :userId ORDER BY dateTime ASC")
    fun getAllByUserIdFlow(userId: UUID): Flow<List<Notification>>

    @Query("SELECT * FROM notification WHERE recurrenceId = :recurrenceId")
    fun getAllByRecurrenceId(recurrenceId: UUID): List<Notification>

    @Query("SELECT * FROM notification WHERE recurrenceId = :recurrenceId")
    fun getAllByRecurrenceIdFlow(recurrenceId: UUID): Flow<List<Notification>>
}

@Dao
interface RecurrencesWithNotificationsDao {
    @Query("SELECT * FROM recurrence WHERE userId = :userId")
    fun getAllByUserId(userId: UUID): Flow<List<RecurrenceWithNotification>>
}

@Dao
interface AccountsDao {
    @Upsert
    suspend fun upsert(account: Account)

    @Delete
    suspend fun delete(account: Account)

    @Query("DELETE FROM account WHERE accountId = :accountId")
    suspend fun deleteByAccountId(accountId: UUID)

    @Query("UPDATE account SET isFavorite = :isFavorite WHERE accountId = :accountId AND userId = :userId")
    suspend fun setIsFavorite(accountId: UUID, userId: UUID, isFavorite: Boolean)

    @Query("SELECT * FROM account WHERE userId = :userId ORDER BY isFavorite DESC, balance DESC, title ASC")
    fun getAllByUserId(userId: UUID): Flow<List<Account>>

    @Query("SELECT * FROM account WHERE accountId = :accountId")
    fun getByAccountId(accountId: UUID): Account

    @Query("SELECT * FROM account WHERE accountId = :accountId")
    fun getByAccountIdFlow(accountId: UUID): Flow<Account>

    @Query("SELECT * FROM account WHERE accountId = :accountId")
    fun getByAccountIdOrNull(accountId: UUID): Account?

    @Query("SELECT * FROM account WHERE LOWER(title) = LOWER(:title) AND userId = :userId")
    fun getByTitleOrNull(title: String, userId: UUID): Account?

    @Query("SELECT SUM(balance) FROM account WHERE userId = :userId")
    fun getTotalBalance(userId: UUID): Flow<Double>

    @Query("UPDATE account SET balance = balance + :amount WHERE accountId = :accountId")
    suspend fun updateBalance(accountId: UUID, amount: Double)

    @Query("SELECT type FROM account WHERE LOWER(title) = LOWER(:title) AND userId = :userId")
    fun getTypeByTitle(title: String, userId: UUID): Flow<String>

    @Query("SELECT type FROM account WHERE accountId = :accountId")
    fun getTypeByAccountId(accountId: UUID): Flow<String>
}

@Dao
interface CustomCategoriesDao {
    @Upsert
    suspend fun upsert(customCategory: CustomCategory)

    @Delete
    suspend fun delete(customCategory: CustomCategory)

    @Query("SELECT * FROM customcategory WHERE userId = :userId GROUP BY type")
    fun getByUserId(userId: UUID): Flow<List<CustomCategory>>
}

@Dao
interface AchievementsDao {
    @Upsert
    suspend fun upsert(achievement: Achievement)

    @Delete
    suspend fun delete(achievement: Achievement)

    @Query("SELECT * FROM achievement WHERE userId = :userId GROUP BY name ORDER BY achievedOn DESC, level DESC")
    fun getByUserId(userId: UUID): Flow<List<Achievement>>
}