package com.cashflowtracker.miranda.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.ui.theme.LocalCustomColors
import com.cashflowtracker.miranda.utils.CategoryClass
import com.cashflowtracker.miranda.utils.DefaultCategories
import com.cashflowtracker.miranda.utils.DescriptionCategory
import com.cashflowtracker.miranda.utils.SpecialType
import io.ktor.util.reflect.instanceOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDescriptionBottomSheet(
    showDialog: MutableState<Boolean>,
    selectedItem: MutableState<DescriptionCategory?>
) {
    val context = LocalContext.current
    ModalBottomSheet(
        onDismissRequest = { showDialog.value = false },
    ) {
        when (selectedItem.value) {
            is DefaultCategories -> {
                CategoryListItem(
                    category = selectedItem.value as DefaultCategories,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    boxSize = 52.dp,
                    iconSize = 32.dp,
                    starSize = 28.dp,
                    containerColor = Color.Transparent,
                    textStyle = MaterialTheme.typography.titleLarge,
                    textPaddingStart = 4.dp
                )
            }

            is SpecialType -> {
                SpecialListItem(
                    special = selectedItem.value as SpecialType,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    boxSize = 52.dp,
                    iconSize = 32.dp,
                    containerColor = Color.Transparent,
                    textStyle = MaterialTheme.typography.titleLarge,
                    textPaddingStart = 4.dp
                )
            }
        }
        Text(selectedItem.value!!.description)
        Row(modifier = Modifier.fillMaxWidth()) {

        }
    }
}