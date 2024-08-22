package com.cashflowtracker.miranda.ui.screens

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.utils.Routes

class SelectAccountType : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val initialAccountType = intent.getStringExtra("accountType") ?: ""
        setContent {
            val accountType = remember { mutableStateOf(initialAccountType) }
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        title = { Text("") },
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
                LazyColumn(modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .clickable {
                        val resultIntent = Intent().putExtra("accountType", "Bank")
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }) {

                }
            }
        }
    }
}