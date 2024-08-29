package com.cashflowtracker.miranda.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getCurrentUserId
import com.cashflowtracker.miranda.ui.composables.FullScreenMapView
import com.cashflowtracker.miranda.ui.theme.MirandaTheme
import com.cashflowtracker.miranda.ui.viewmodels.TransactionsViewModel
import org.koin.androidx.compose.koinViewModel

class MapView : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val vm = koinViewModel<TransactionsViewModel>()
            val userId = context.getCurrentUserId()
            val transactions by vm.actions.getAllWithLocationByUserIdFlow(userId)
                .collectAsState(initial = emptyList())

            MirandaTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { },
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
                            }
                        )
                    }
                ) { _ ->
                    if (transactions.isNotEmpty()) {
                        FullScreenMapView(transactions)
                    }
                }
            }

        }
    }
}