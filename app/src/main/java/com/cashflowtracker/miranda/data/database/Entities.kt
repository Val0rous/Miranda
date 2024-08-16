package com.cashflowtracker.miranda.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.ZonedDateTime
import java.util.UUID

@Entity(indices = [Index(value = ["email"], unique = true)])
data class User(
    @PrimaryKey val userId: UUID,   // Useful in case the user changes their email address
    @ColumnInfo val name: String,
    @ColumnInfo val email: String,
    @ColumnInfo val password: String,
    @ColumnInfo val salt: String?,
    @ColumnInfo val encryptionKey: String?,  // Could be useful to encrypt data and password
    @ColumnInfo val currency: String?,   // Maybe it's useless (SharedPreferences)
    @ColumnInfo val country: String?,    // Maybe it's useless, too
)

@Entity
data class TransactionLog(
    @PrimaryKey val transactionId: String,
    @ColumnInfo val type: String,
    @ColumnInfo val dateTime: ZonedDateTime,
    @ColumnInfo val source: String,
    @ColumnInfo val destination: String,
    @ColumnInfo val amount: Double,
    @ColumnInfo val currency: String,
    @ColumnInfo val comment: String?,
    @ColumnInfo val location: String?,
    @ColumnInfo val userId: UUID,
)

@Entity
data class Recurrence(
    @PrimaryKey val recurrenceId: String,
    @ColumnInfo val type: String,
    @ColumnInfo val dateTime: ZonedDateTime,
    @ColumnInfo val source: String,
    @ColumnInfo val destination: String,
    @ColumnInfo val amount: Double,
    @ColumnInfo val currency: String,
    @ColumnInfo val comment: String?,
    @ColumnInfo val location: String?,
    @ColumnInfo val repeatIntervalMillis: Long,
    @ColumnInfo val reoccursOn: ZonedDateTime,
    @ColumnInfo val notifications: List<ZonedDateTime>?, // Up to 5 notification times
    @ColumnInfo val userId: UUID,
)

@Entity
data class Account(
    @PrimaryKey val title: String,
    @ColumnInfo val type: String,
    @ColumnInfo val balance: Double,
    @ColumnInfo val creationDate: ZonedDateTime,
    @ColumnInfo val userId: UUID,
)

@Entity
data class CustomCategory(
    @PrimaryKey val label: String,
    @ColumnInfo val name: String,
    @ColumnInfo val icon: String?,
    @ColumnInfo val type: Char,
    @ColumnInfo val userId: UUID,
)

@Entity
data class Achievement(
    @PrimaryKey val name: String,
    @PrimaryKey val level: Char,
    @ColumnInfo val description: String,
    @ColumnInfo val icon: String,
    @ColumnInfo val achievedOn: ZonedDateTime,
    @ColumnInfo val userId: UUID,
)