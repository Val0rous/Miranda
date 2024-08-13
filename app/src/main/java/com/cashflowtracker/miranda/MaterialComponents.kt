package com.cashflowtracker.miranda

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R

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
            // ScrollableList()
            MaterialList()
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
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = selectedItem == 0,
            onClick = { selectedItem = 0 }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Assignment, contentDescription = "Transactions") },
            label = { Text("Transactions") },
            selected = selectedItem == 1,
            onClick = { selectedItem = 1 }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Schedule, contentDescription = "Recurrents") },
            label = { Text("Home") },
            selected = selectedItem == 2,
            onClick = { selectedItem = 2 }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Leaderboard, contentDescription = "Stats") },
            label = { Text("Stats") },
            selected = selectedItem == 3,
            onClick = { selectedItem = 3 }
        )
    }
}
