package com.tutorial.introtocompose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tutorial.introtocompose.ui.theme.IntroToComposeTheme
import com.tutorial.introtocompose.ui.theme.Purple200

/**
 * Reference: https://developer.android.com/jetpack/compose/tutorial
 * @Surface: 基本容器，顯示在螢幕上的東西
 * @Column: 垂直排列元素
 * @Row: 水平排列元素
 * @Box: 堆疊元素
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IntroToComposeTheme {
                DefaultPreview()
            }
        }
    }
}

@Composable
fun MyApp() {
    // remember: 用來記住物件的狀態
    val moneyCounter = remember { mutableStateOf(0) }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFECEFF1)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$${moneyCounter.value}",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            CreateCircle(moneyCounter.value) { money ->
                moneyCounter.value += money
            }

            if (moneyCounter.value > 10)
                Text(text = "You are rich!", modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Composable
fun CreateCircle(moneyCounter: Int = 0, update: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .padding(3.dp)
            .size(105.dp)
            .clickable {
                update(1)

                Log.d("MainActivity", "moneyCount: $moneyCounter")
            },
        shape = CircleShape,
        border = BorderStroke(2.dp, Color.Black),
        backgroundColor = Purple200,
        elevation = 4.dp,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = "Tap $moneyCounter", color = Color.Black)
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(
    showBackground = true,
    name = "Dark Mode",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun DefaultPreview() {
    IntroToComposeTheme {
        MyApp()
    }
}