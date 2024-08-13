package com.cashflowtracker.miranda

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.Assignment
import androidx.compose.material.icons.rounded.CreditCard
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Leaderboard
import androidx.compose.material.icons.rounded.QueryStats
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.VpnKey
import androidx.compose.material.icons.rounded.Wallet
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

// class account
data class Account(val name: String, val balance: String, val icon: ImageVector, val isFavorite: Boolean)

@Composable
fun AppLayout() {
    Scaffold(
        topBar = { AppBar() },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.Add, "Add item")
            }
        },
        bottomBar = { Navbar() }
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            // Aggiungi il saldo totale , provvisorio da aggiungere la card..
            TotalBalance("3,470.00 €")
            // Aggiungi la lista degli account
            val accounts = listOf(
                Account("N26", "420.00 €", Icons.Rounded.AccountBalance, true),
                Account("Hype", "360.00 €", Icons.Rounded.CreditCard, true),
                Account("Wallet", "100.00 €", Icons.Rounded.Wallet, false),
                Account("Deutsche Bank", "1,500.00 €", Icons.Filled.AccountBalance, false),
                Account("Investments", "940.00 €", Icons.Rounded.QueryStats, false),
                Account("Safe", "400.00 €", Icons.Rounded.VpnKey, false),
                Account("Revolut", "200.00 €", Icons.Rounded.CreditCard, false)
            )
            AccountList(accounts)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar() {
    TopAppBar(
        title = { Text("") },
//        navigationIcon = {
//            IconButton(onClick = { /*TODO*/ }) {
//                Icon(Icons.Filled.Menu, "Menu")
//            }
//        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Rounded.Settings, "Settings")
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.AccountCircle, "Profile")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}

@Composable
fun MaterialList() {
    val elems = (0..100).toList().map { "Elem $it" }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(4.dp)
    ) {
        items(elems) {
            MaterialListItem(it)
        }
    }
}

@Composable
fun MaterialListItem(item: String, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp)
    ) {
//        Image(
//            painter = painterResource(R.drawable.ic_launcher_foreground),
//            contentDescription = "Android Logo",
//            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer),
//            modifier = Modifier.size(48.dp)
//        )
        Icon(
            imageVector = Icons.Rounded.AccountBalance,
            contentDescription = "Account",
            modifier = Modifier.size(40.dp)
        )
        Spacer(Modifier.size(16.dp))
        Text(
            item,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
fun Navbar() {
    var selectedItem by remember { mutableIntStateOf(0) }

    NavigationBar {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = if (selectedItem == 0) Icons.Filled.Home else Icons.Rounded.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Home") },
            selected = selectedItem == 0,
            onClick = { selectedItem = 0 }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = if (selectedItem == 1) Icons.Filled.Assignment else Icons.Rounded.Assignment,
                    contentDescription = "Transactions"
                )
            },
            label = { Text("Transactions") },
            selected = selectedItem == 1,
            onClick = { selectedItem = 1 }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = if (selectedItem == 2) Icons.Filled.Schedule else Icons.Rounded.Schedule,
                    contentDescription = "Recurrents"
                )
            },
            label = { Text("Recurrents") },
            selected = selectedItem == 2,
            onClick = { selectedItem = 2 }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = if (selectedItem == 3) Icons.Filled.Leaderboard else Icons.Rounded.Leaderboard,
                    contentDescription = "Stats"
                )
            },
            label = { Text("Stats") },
            selected = selectedItem == 3,
            onClick = { selectedItem = 3 }
        )
    }
}


@Composable
fun TotalBalance(balance: String) {
    OutlinedCard(modifier = Modifier.padding(8.dp)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = "Total Balance",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = balance,
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun AccountList(accounts: List<Account>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        items(accounts) { account ->
            AccountItem(account)
        }
    }
}

@Composable
fun AccountItem(account: Account) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Icon(
            imageVector = account.icon,
            contentDescription = account.name,
            modifier = Modifier.size(40.dp)
        )
        Spacer(Modifier.size(16.dp))
        Column {
            Text(
                text = account.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = account.balance,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (account.isFavorite) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = "Favorite",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
