package com.cashflowtracker.miranda

import androidx.room.Room
import com.cashflowtracker.miranda.data.database.MirandaDatabase


val db = Room.databaseBuilder(
    applicationContext,
    MirandaDatabase::class.java,
    "database"
).build()