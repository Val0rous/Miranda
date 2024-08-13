package com.cashflowtracker.miranda

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashflowtracker.miranda.ui.theme.JetpackComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeTheme {
                val config = LocalConfiguration.current
                // A surface container using the 'background' color from the theme
                Surface(
                    // 2. Change surface background color
                    color = MaterialTheme.colorScheme.background,
                    // color = Purple80,
                    // 3. Change surface size
                    modifier = Modifier.fillMaxSize(),
                    /*modifier = Modifier.size(
                        config.screenWidthDp.dp / 2,
                        config.screenHeightDp.dp
                    )*/
                ) {
                    // Greeting("Android")
                    // Orientation(config)
                    // TextStyle()
                    // HorizontalLayout()
                    // VerticalLayout()
                    // BoxWithConstraintsLayout()
                    // DynamicContent(listOf("First", "Second", "Third"))
                    // ScrollableList()
                    AppLayout()
                }
            }
        }
    }
}

// 4. Detect device orientation
@Composable
fun Orientation(config: Configuration) {
    Text(when(config.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> "Portrait"
        Configuration.ORIENTATION_LANDSCAPE -> "Landscape"
        else -> "Unknown"
    })
}

// 5. Change the text style
@Composable
fun TextStyle() {
    Text(
        "Hello, World!",
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Italic
    )
}

// 6. Create a row of text items
@Composable
fun HorizontalLayout() {
    Row {
        Text("First item")
        Text("Second item")
        Text("Third item")
    }
}

// 7. Create a column of text items
@Composable
fun VerticalLayout() {
    Column {
        Text("First")
        Text("Second")
        Text("Third")
    }
}

// 8. Detect container size
@Composable
fun BoxWithConstraintsLayout() {
    BoxWithConstraints {
        // Salva lo scope del BoxWithConstraints per utilizzarlo negli altri composable
        val box = this

        Column {
            if (box.maxHeight >= 400.dp) {
                Text(">= 400.dp")
                Spacer(modifier = Modifier.size(10.dp))
            }
            Text("""
                minHeight: ${box.minHeight}
                maxHeight: ${box.maxHeight}
                minWidth: ${box.minWidth}
                maxWidth: ${box.maxWidth}
            """.trimIndent())
        }
    }
}

// 9. Handle dynamic content
@Composable
fun DynamicContent(items: List<String>) {
    Column {
        for (item in items) {
            Text(item)
        }
    }
}

// 10. Create a scrollable list
@Composable
fun SlowScrollableList() {
    val elems = (0..100).toList().map { "Elem $it" }
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.verticalScroll(scrollState)) {
        for (elem in elems) Text(elem)
    }
}
@Composable
fun ScrollableList() {
    val elems = (0..100).toList().map { "Elem $it" }

    LazyColumn {
        items(elems) {
            Text(it)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetpackComposeTheme {
        Greeting("Android")
    }
}
