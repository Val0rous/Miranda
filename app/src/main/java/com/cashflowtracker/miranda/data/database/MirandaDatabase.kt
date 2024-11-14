package com.cashflowtracker.miranda.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [User::class, Transaction::class, Recurrence::class, Notification::class, Account::class, CustomCategory::class, Achievement::class],
    version = 1
)
abstract class MirandaDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDao
    abstract fun transactionsDao(): TransactionsDao
    abstract fun recurrencesDao(): RecurrencesDao
    abstract fun recurrencesWithNotificationsDao(): RecurrencesWithNotificationsDao
    abstract fun notificationsDao(): NotificationsDao
    abstract fun accountsDao(): AccountsDao
    abstract fun customCategoriesDao(): CustomCategoriesDao
    abstract fun achievementsDao(): AchievementsDao
}