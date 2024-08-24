package com.cashflowtracker.miranda

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.cashflowtracker.miranda.data.database.MirandaDatabase
import com.cashflowtracker.miranda.data.repositories.AccountsRepository
import com.cashflowtracker.miranda.data.repositories.ThemeRepository
import com.cashflowtracker.miranda.data.repositories.UsersRepository
import com.cashflowtracker.miranda.ui.viewmodels.ThemeViewModel
import com.cashflowtracker.miranda.ui.viewmodels.UsersViewModel
import com.cashflowtracker.miranda.ui.viewmodels.AccountsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore("settings")

val appModule = module {
    single { get<Context>().dataStore }

    single {
        Room.databaseBuilder(
            get(),
            MirandaDatabase::class.java,
            "miranda"
        ).build()
    }

    single { UsersRepository(get<MirandaDatabase>().usersDao()) }
    single { AccountsRepository(get<MirandaDatabase>().accountsDao()) }

    viewModel { ThemeViewModel(androidContext()) }
    viewModel { UsersViewModel(get()) }
    viewModel { AccountsViewModel(get()) }
}
