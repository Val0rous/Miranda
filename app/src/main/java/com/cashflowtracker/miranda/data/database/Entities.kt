package com.cashflowtracker.miranda.data.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.UUID

@Entity(indices = [Index(value = ["email"], unique = true)])
data class User(
    @PrimaryKey val userId: UUID = UUID.randomUUID(),   // Useful in case the user changes their email address
    @ColumnInfo val name: String,
    @ColumnInfo val email: String,
    @ColumnInfo val password: String,
    @ColumnInfo val salt: String,
    @ColumnInfo val encryptionKey: String = "",  // Could be useful to encrypt data and password
    @ColumnInfo val currency: String = "USD",
    @ColumnInfo val country: String = "UNITED_STATES",
)

sealed class BaseTransaction {
    abstract val type: String
    abstract val source: String
    abstract val destination: String
    abstract val comment: String?
    abstract val location: String?
}

@Entity
data class Transaction(
    @PrimaryKey val transactionId: UUID = UUID.randomUUID(),
    @ColumnInfo override val type: String,
    @ColumnInfo val createdOn: String,   // ZonedDateTime
    @ColumnInfo override val source: String,
    @ColumnInfo override val destination: String,
    @ColumnInfo val amount: Double,
    @ColumnInfo val currency: String,
    @ColumnInfo override val comment: String,
    @ColumnInfo override val location: String,
    @ColumnInfo val userId: UUID,
    @ColumnInfo val isCreatedByRecurrence: Boolean = false,
) : BaseTransaction()

@Entity
data class Recurrence(
    @PrimaryKey val recurrenceId: UUID = UUID.randomUUID(),
    @ColumnInfo override val type: String,
    @ColumnInfo val createdOn: String,   // ZonedDateTime
    @ColumnInfo override val source: String,
    @ColumnInfo override val destination: String,
    @ColumnInfo val amount: Double,
    @ColumnInfo val currency: String,
    @ColumnInfo override val comment: String,
    @ColumnInfo override val location: String,
    @ColumnInfo val repeatInterval: String, // Repeats.name
    @ColumnInfo val reoccursOn: String,   // ZonedDateTime
    @ColumnInfo val userId: UUID,
) : BaseTransaction()

@Entity
data class Notification(
    @PrimaryKey val notificationId: UUID = UUID.randomUUID(),
    @ColumnInfo val dateTime: String,   // ZonedDateTime
    @ColumnInfo val notificationType: String, // Notifications.name
    @ColumnInfo val recurrenceId: UUID,
    @ColumnInfo val userId: UUID,
)

@Entity
data class RecurrenceWithNotification(
    @Embedded val recurrence: Recurrence,
    @Relation(
        parentColumn = "recurrenceId",  // Column in Recurrence
        entityColumn = "recurrenceId",  // Column in Notification
    )
    val notifications: List<Notification>,
)

@Entity
data class Account(
    @PrimaryKey val accountId: UUID = UUID.randomUUID(),
    @ColumnInfo val title: String,
    @ColumnInfo val type: String,
    @ColumnInfo val balance: Double,
    @ColumnInfo val createdOn: String,   // ZonedDateTime
    @ColumnInfo val userId: UUID,
    @ColumnInfo val isFavorite: Boolean,
)

@Entity
data class CustomCategory(
    @PrimaryKey val label: String,
    @ColumnInfo val name: String,
    @ColumnInfo val icon: String?,
    @ColumnInfo val type: Char,
    @ColumnInfo val userId: UUID,
)

@Entity(primaryKeys = ["name", "level"])
data class Achievement(
    val name: String,
    val level: Char,
    @ColumnInfo val description: String,
    @ColumnInfo val icon: String,
    @ColumnInfo val achievedOn: String, // ZonedDateTime
    @ColumnInfo val userId: UUID,
)