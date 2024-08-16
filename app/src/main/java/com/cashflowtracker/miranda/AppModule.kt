package com.cashflowtracker.miranda

import androidx.room.Room
import com.cashflowtracker.miranda.data.database.MirandaDB


val db = Room.databaseBuilder(
    applicationContext,
    MirandaDB::class.java,
    "database"
).build()