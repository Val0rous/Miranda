package com.cashflowtracker.miranda.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class MirandaDB : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun transactionDao(): TransactionDao
    abstract fun RecurrenceDao(): RecurrenceDao
    abstract fun AccountDao(): AccountDao
    abstract fun CustomCategoryDao(): CustomCategoryDao
    abstract fun AchievementDao(): AchievementDao
}