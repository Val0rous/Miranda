package com.cashflowtracker.miranda.ui.composables

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.ui.theme.LocalCustomColors
import com.cashflowtracker.miranda.utils.CategoryClass
import com.cashflowtracker.miranda.utils.DefaultCategories
import com.cashflowtracker.miranda.utils.DescriptionCategory
import com.cashflowtracker.miranda.utils.SpecialType
import io.ktor.util.reflect.instanceOf
import org.koin.core.component.getScopeName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDescriptionBottomSheet(
    showDialog: MutableState<Boolean>,
    selectedItem: MutableState<DescriptionCategory?>,
    isSource: Boolean, //true if source, false if destination
    activity: Activity
) {
    val title = if (isSource) "sourceTitle" else "destinationTitle"
    val icon = if (isSource) "sourceIcon" else "destinationIcon"
    val context = LocalContext.current
    val colorScheme = MaterialTheme.colorScheme
    val color = remember { mutableStateOf(colorScheme.surfaceTint) }
    val category = remember { mutableStateOf("") }
    ModalBottomSheet(
        onDismissRequest = { showDialog.value = false },
    ) {
        when (selectedItem.value) {
            is DefaultCategories -> {
                when ((selectedItem.value as DefaultCategories).type) {
                    CategoryClass.NECESSITY -> color.value =
                        LocalCustomColors.current.surfaceTintRed

                    CategoryClass.CONVENIENCE -> color.value =
                        LocalCustomColors.current.surfaceTintYellow

                    CategoryClass.LUXURY -> color.value = LocalCustomColors.current.surfaceTintGreen
                    CategoryClass.UNRANKED -> {}
                }
                category.value =
                    stringResource((selectedItem.value as DefaultCategories).type.label)
                CategoryListItem(
                    category = selectedItem.value as DefaultCategories,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    boxSize = 52.dp,
                    iconSize = 32.dp,
                    starSize = 28.dp,
                    containerColor = Color.Transparent,
                    textStyle = MaterialTheme.typography.titleLarge,
                    textPaddingStart = 4.dp,
                    showCategory = true,
                    chipColor = color.value
                )
            }

            is SpecialType -> {
                color.value = LocalCustomColors.current.surfaceTintDeepPurple
                category.value = stringResource(R.string.category_class_special)
                SpecialListItem(
                    special = selectedItem.value as SpecialType,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    boxSize = 52.dp,
                    iconSize = 32.dp,
                    containerColor = Color.Transparent,
                    textStyle = MaterialTheme.typography.titleLarge,
                    textPaddingStart = 4.dp,
                    showSpecial = true,
                    chipColor = color.value
                )
            }
        }
        if (selectedItem.value!!.description != 0) {
            Text(
                text = stringResource(selectedItem.value!!.description),
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 4.dp, bottom = 8.dp),
            horizontalArrangement = spacedBy(12.dp)
        ) {
            TextButton(
                onClick = { showDialog.value = false },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Cancel")
            }
            FilledTonalButton(
                onClick = {
                    val resultIntent = Intent()
                        .putExtra(title, (selectedItem.value!! as Enum<*>).name)
                        .putExtra(icon, selectedItem.value!!.icon.toString())
                    activity.setResult(RESULT_OK, resultIntent)
                    activity.finish()
                    showDialog.value = false
                },
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = color.value,
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Select",
                    color = LocalCustomColors.current.icon
                )
            }
        }
    }
}