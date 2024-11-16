package com.cashflowtracker.miranda.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.database.Recurrence
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getCurrentUserId
import com.cashflowtracker.miranda.ui.composables.AlertDialogIconTitle
import com.cashflowtracker.miranda.ui.composables.MapScreen
import com.cashflowtracker.miranda.ui.theme.Green400
import com.cashflowtracker.miranda.ui.theme.LocalCustomColors
import com.cashflowtracker.miranda.ui.theme.MirandaTheme
import com.cashflowtracker.miranda.ui.theme.Red400
import com.cashflowtracker.miranda.ui.theme.Yellow400
import com.cashflowtracker.miranda.ui.viewmodels.AccountsViewModel
import com.cashflowtracker.miranda.ui.viewmodels.RecurrencesViewModel
import com.cashflowtracker.miranda.utils.AccountType
import com.cashflowtracker.miranda.utils.CategoryClass
import com.cashflowtracker.miranda.utils.Coordinates
import com.cashflowtracker.miranda.utils.Currencies
import com.cashflowtracker.miranda.utils.DefaultCategories
import com.cashflowtracker.miranda.utils.SpecialType
import com.cashflowtracker.miranda.utils.formatAmount
import com.cashflowtracker.miranda.utils.formatZonedDateTime
import com.cashflowtracker.miranda.utils.revertTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import java.util.UUID

class ViewRecurrence : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val initialRecurrenceId = UUID.fromString(intent.getStringExtra("recurrenceId") ?: "")
        setContent {
            val userId = LocalContext.current.getCurrentUserId()
            val recurrenceId by remember { mutableStateOf(initialRecurrenceId) }
            val coroutineScope = rememberCoroutineScope()
            val vm = koinViewModel<RecurrencesViewModel>()
            var isLoaded by remember { mutableStateOf(false) }
            var recurrence by remember { mutableStateOf<Recurrence?>(null) }
            var isDeleting by remember { mutableStateOf(false) }
            val openAlertDialog = remember { mutableStateOf(false) }
            var sourceType by remember { mutableStateOf("") }
            var destinationType by remember { mutableStateOf("") }
            val accountsVm = koinViewModel<AccountsViewModel>()
            val isLocationLoaded = remember { mutableStateOf(false) }
            val coordinates = remember { mutableStateOf<Coordinates?>(null) }

            LaunchedEffect(key1 = recurrenceId, key2 = isDeleting) {
                if (!isDeleting) {
                    coroutineScope.launch(Dispatchers.IO) {
                        vm.actions.getByRecurrenceIdFlow(recurrenceId)
                            .collect {
                                withContext(Dispatchers.Main) {
                                    recurrence = it.also {
                                        if (!isDeleting) {
                                            isLocationLoaded.value = !it.location.isNullOrEmpty()

                                            coroutineScope.launch(Dispatchers.IO) {
                                                if (!isDeleting) {
                                                    if (it.type == "Output" || it.type == "Transfer") {
                                                        accountsVm.actions.getTypeByTitle(
                                                            it.source,
                                                            userId
                                                        ).collect { item ->
                                                            sourceType = item
                                                        }
                                                    }
                                                }
                                            }

                                            coroutineScope.launch(Dispatchers.IO) {
                                                if (!isDeleting) {
                                                    if (it.type == "Input" || it.type == "Transfer") {
                                                        accountsVm.actions.getTypeByTitle(
                                                            it.destination,
                                                            userId
                                                        ).collect { item ->
                                                            destinationType = item
                                                        }
                                                    }
                                                }
                                            }

                                            coroutineScope.launch {
                                                if (isLocationLoaded.value) {
                                                    it.location?.split(", ", limit = 2)
                                                        ?.let { item ->
                                                            if (item.size == 2) {
                                                                coordinates.value =
                                                                    Coordinates(
                                                                        item[0].toDouble(),
                                                                        item[1].toDouble()
                                                                    )
                                                            }
                                                        }
                                                }
                                            }

                                            isLoaded = true
                                        }
                                    }
                                }
                            }
                    }
                }
            }

            MirandaTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                if (isLoaded) {
                                    if (!isDeleting) {
                                        Text(recurrence!!.type)
                                    }
                                }
                            },
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
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        ) {
                            NavigationBarItem(
                                selected = false,
                                label = { Text("Edit") },
                                icon = {
                                    Icon(
                                        ImageVector.vectorResource(R.drawable.ic_edit),
                                        contentDescription = "Edit"
                                    )
                                },
                                onClick = {
//                                    val intent =
//                                        Intent(
//                                            this@ViewRecurrence,
//                                            EditRecurrence::class.java
//                                        )
//                                    intent.putExtra("recurrenceId", recurrenceId.toString())
//                                    startActivity(intent)
                                }
                            )
                            NavigationBarItem(
                                selected = false,
                                label = { Text("Delete") },
                                icon = {
                                    Icon(
                                        ImageVector.vectorResource(R.drawable.ic_delete),
                                        contentDescription = "Delete"
                                    )
                                },
                                enabled = !isDeleting,
                                onClick = {
                                    openAlertDialog.value = true
                                },
                            )
                        }
                    }
                ) { paddingValues ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            if (isLoaded) {
                                if (!isDeleting) {
                                    Row(
                                        verticalAlignment = Alignment.Top,
                                        modifier = Modifier.padding(bottom = 24.dp)
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.width(128.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(56.dp)
                                                    .clip(CircleShape)
                                                    .background(
                                                        when (recurrence!!.type) {
                                                            "Output" -> LocalCustomColors.current.surfaceTintRed
                                                            "Input" -> LocalCustomColors.current.surfaceTintGreen
                                                            else -> LocalCustomColors.current.surfaceTintBlue
                                                        }
                                                    )
                                            ) {
                                                Icon(
                                                    imageVector = ImageVector.vectorResource(
                                                        when (recurrence!!.type) {
                                                            "Output" -> AccountType.getIcon(
                                                                sourceType
                                                            )

                                                            "Input" -> {
                                                                when (recurrence!!.source) {
                                                                    SpecialType.POCKET.category, SpecialType.EXTRA.category -> SpecialType.getIcon(
                                                                        recurrence!!.source
                                                                    )

                                                                    else -> DefaultCategories.getIcon(
                                                                        recurrence!!.source
                                                                    )
                                                                }
                                                            }

                                                            else -> AccountType.getIcon(sourceType)
                                                        }
                                                    ),
                                                    contentDescription = recurrence!!.source,
                                                    tint = LocalCustomColors.current.icon,
                                                    modifier = Modifier
                                                        .size(40.dp)
                                                        .align(Alignment.Center)
                                                )
                                            }
                                            Text(
                                                text = recurrence!!.source,
                                                style = MaterialTheme.typography.titleMedium,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.padding(top = 8.dp)
                                            )
                                            if (recurrence!!.type == "Input") {
                                                Row(modifier = Modifier.padding(top = 4.dp)) {
                                                    when (DefaultCategories.getType(recurrence!!.source)) {
                                                        CategoryClass.NECESSITY -> repeat(1) {
                                                            Icon(
                                                                imageVector = ImageVector.vectorResource(
                                                                    R.drawable.ic_star_filled
                                                                ),
                                                                contentDescription = "",
                                                                tint = Red400,
                                                                modifier = Modifier
                                                                    .size(24.dp)
                                                            )
                                                        }

                                                        CategoryClass.CONVENIENCE -> repeat(2) {
                                                            Icon(
                                                                imageVector = ImageVector.vectorResource(
                                                                    R.drawable.ic_star_filled
                                                                ),
                                                                contentDescription = "",
                                                                tint = Yellow400,
                                                                modifier = Modifier
                                                                    .size(24.dp)
                                                            )
                                                        }

                                                        CategoryClass.LUXURY -> repeat(3) {
                                                            Icon(
                                                                imageVector = ImageVector.vectorResource(
                                                                    R.drawable.ic_star_filled
                                                                ),
                                                                contentDescription = "",
                                                                tint = Green400,
                                                                modifier = Modifier
                                                                    .size(24.dp)
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.ic_east),
                                            contentDescription = "To",
                                            tint = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier
                                                .size(24.dp)
                                                .offset(y = 16.dp)
                                        )

                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier
                                                .width(128.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(56.dp)
                                                    .clip(CircleShape)
                                                    .background(
                                                        when (recurrence!!.type) {
                                                            "Output" -> LocalCustomColors.current.surfaceTintRed
                                                            "Input" -> LocalCustomColors.current.surfaceTintGreen
                                                            else -> LocalCustomColors.current.surfaceTintBlue
                                                        }
                                                    )
                                            ) {
                                                Icon(
                                                    imageVector = ImageVector.vectorResource(
                                                        when (recurrence!!.type) {
                                                            "Output" -> DefaultCategories.getIcon(
                                                                recurrence!!.destination
                                                            )

                                                            "Input" -> AccountType.getIcon(
                                                                destinationType
                                                            )

                                                            else -> AccountType.getIcon(
                                                                destinationType
                                                            )
                                                        }
                                                    ),
                                                    contentDescription = recurrence!!.destination,
                                                    tint = LocalCustomColors.current.icon,
                                                    modifier = Modifier
                                                        .size(40.dp)
                                                        .align(Alignment.Center)
                                                )
                                            }
                                            Text(
                                                text = recurrence!!.destination,
                                                style = MaterialTheme.typography.titleMedium,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                modifier = Modifier.padding(top = 8.dp)
                                            )
                                            if (recurrence!!.type == "Output") {
                                                Row(modifier = Modifier.padding(top = 4.dp)) {
                                                    when (DefaultCategories.getType(recurrence!!.destination)) {
                                                        CategoryClass.NECESSITY -> repeat(1) {
                                                            Icon(
                                                                imageVector = ImageVector.vectorResource(
                                                                    R.drawable.ic_star_filled
                                                                ),
                                                                contentDescription = "",
                                                                tint = Red400,
                                                                modifier = Modifier
                                                                    .size(24.dp)
                                                            )
                                                        }

                                                        CategoryClass.CONVENIENCE -> repeat(2) {
                                                            Icon(
                                                                imageVector = ImageVector.vectorResource(
                                                                    R.drawable.ic_star_filled
                                                                ),
                                                                contentDescription = "",
                                                                tint = Yellow400,
                                                                modifier = Modifier
                                                                    .size(24.dp)
                                                            )
                                                        }

                                                        CategoryClass.LUXURY -> repeat(3) {
                                                            Icon(
                                                                imageVector = ImageVector.vectorResource(
                                                                    R.drawable.ic_star_filled
                                                                ),
                                                                contentDescription = "",
                                                                tint = Green400,
                                                                modifier = Modifier
                                                                    .size(24.dp)
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    HorizontalDivider(modifier = Modifier.padding(bottom = 24.dp))

                                    if (recurrence!!.createdOn.isNotEmpty()) {
                                        Text(
                                            text = formatZonedDateTime(recurrence!!.createdOn),
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    Text(
                                        text = formatAmount(
                                            recurrence!!.amount,
                                            Currencies.get(recurrence!!.currency),
                                            recurrence!!.type
                                        ),
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = when (recurrence!!.type) {
                                            "Output" -> LocalCustomColors.current.surfaceTintRed
                                            "Input" -> LocalCustomColors.current.surfaceTintGreen
                                            else -> LocalCustomColors.current.surfaceTintBlue
                                        },
                                        modifier = Modifier.padding(top = 24.dp)
                                    )

                                    if (recurrence!!.comment.isNotEmpty()) {
                                        Text(
                                            text = recurrence!!.comment,
                                            style = MaterialTheme.typography.headlineMedium,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.padding(top = 24.dp)
                                        )
                                    }

                                    HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))

                                    if (coordinates.value != null) {
                                        MapScreen(
                                            latitude = coordinates.value?.latitude ?: 0.0,
                                            longitude = coordinates.value?.longitude ?: 0.0,
                                            isLocationLoaded = isLocationLoaded,
                                            modifier = Modifier.padding(horizontal = 16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (openAlertDialog.value) {
                        AlertDialogIconTitle(
                            icon = R.drawable.ic_delete,
                            onDismissRequest = {
                                openAlertDialog.value = false
                            },
                            onConfirmation = {
                                openAlertDialog.value = false
                                isDeleting = true
                                coroutineScope.launch(Dispatchers.IO) {
                                    vm.actions.removeRecurrence(recurrenceId)
                                    finish()
                                }
                            },
                            dialogTitle = "Delete recurrence",
                            dialogText = "This operation is irreversible. No future transactions will be made, but existing associated ones will not be affected",
                            actionText = "Delete"
                        )
                    }
                }
            }
        }
    }
}