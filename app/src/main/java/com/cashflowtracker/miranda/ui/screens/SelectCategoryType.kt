package com.cashflowtracker.miranda.ui.screens

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.ui.theme.Green400
import com.cashflowtracker.miranda.ui.theme.MirandaTheme
import com.cashflowtracker.miranda.ui.theme.Red400
import com.cashflowtracker.miranda.ui.theme.Yellow400
import com.cashflowtracker.miranda.utils.AccountType
import com.cashflowtracker.miranda.utils.CategoryClass
import com.cashflowtracker.miranda.utils.DefaultCategories

class SelectCategoryType : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //val initialAccountType = intent.getStringExtra("accountType") ?: ""
        setContent {
            //val accountType = remember { mutableStateOf(initialAccountType) }
            MirandaTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("Category Types") },
                            navigationIcon = {
                                IconButton(
                                    onClick = { finish() },
                                    modifier = Modifier.padding(
                                        start = 0.dp,
                                        top = 16.dp,
                                        bottom = 16.dp
                                    )
                                ) {
                                    Icon(
                                        ImageVector.vectorResource(R.drawable.ic_arrow_back),
                                        contentDescription = "Back"
                                    )
                                }
                            },
                            actions = {
                                IconButton(
                                    onClick = { /* Logic to search */ },
                                    modifier = Modifier.padding(
                                        top = 16.dp,
                                        bottom = 16.dp,
                                        end = 16.dp
                                    )
                                ) {
                                    Icon(
                                        ImageVector.vectorResource(R.drawable.ic_search),
                                        contentDescription = "Search"
                                    )
                                }
                            }
                        )
                    }
                ) { paddingValues ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        items(DefaultCategories.entries) { categoryType ->
                            ListItem(
                                headlineContent = {
                                    Text(
                                        text = categoryType.category,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                },
                                leadingContent = {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.surfaceTint)
                                    ) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(categoryType.icon),
                                            contentDescription = categoryType.category,
                                            tint = MaterialTheme.colorScheme.surface,
                                            modifier = Modifier
                                                .size(24.dp)
                                                .align(Alignment.Center)
                                        )
                                    }
                                },
                                trailingContent = {
                                    when (categoryType.type) {
                                        CategoryClass.NECESSITY -> {
                                            Row(horizontalArrangement = Arrangement.End) {
                                                repeat(1) {
                                                    Icon(
                                                        imageVector = ImageVector.vectorResource(R.drawable.ic_star_filled),
                                                        contentDescription = "",
                                                        tint = Red400,
                                                        modifier = Modifier
                                                            .size(24.dp)
                                                    )
                                                }
                                            }
                                        }

                                        CategoryClass.CONVENIENCE ->
                                            Row(horizontalArrangement = Arrangement.End) {
                                                repeat(2) {
                                                    Icon(
                                                        imageVector = ImageVector.vectorResource(R.drawable.ic_star_filled),
                                                        contentDescription = "",
                                                        tint = Yellow400,
                                                        modifier = Modifier
                                                            .size(24.dp)
                                                    )
                                                }
                                            }

                                        CategoryClass.LUXURY ->
                                            Row(horizontalArrangement = Arrangement.End) {
                                                repeat(3) {
                                                    Icon(
                                                        imageVector = ImageVector.vectorResource(R.drawable.ic_star_filled),
                                                        contentDescription = "",
                                                        tint = Green400,
                                                        modifier = Modifier
                                                            .size(24.dp)
                                                    )
                                                }
                                            }
                                    }
                                },
                                modifier = Modifier.clickable {
                                    val resultIntent =
                                        Intent().putExtra(
                                            "destinationCategory",
                                            categoryType.category
                                        )
                                            .putExtra(
                                                "destinationIcon",
                                                categoryType.icon.toString()
                                            )
                                    setResult(Activity.RESULT_OK, resultIntent)
                                    finish()
                                }
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}