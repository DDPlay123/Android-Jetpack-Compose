package com.tutorial.bizcard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tutorial.bizcard.ui.theme.BizCardTheme
import androidx.compose.runtime.remember as remember

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BizCardTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    CreateBizCard()
                }
            }
        }
    }
}

@Composable
fun CreateBizCard() {
    val btnClickState = remember { mutableStateOf(false) }
    Surface(modifier = Modifier
        .fillMaxSize()
    ) {
        Card(modifier = Modifier
            .width(200.dp)
            .height(390.dp)
            .padding(12.dp),
            elevation = 4.dp,
            shape = RoundedCornerShape(corner = CornerSize(15.dp))
        ) {
            Column(verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CreateImageProfile()
                Divider(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 15.dp, bottom = 15.dp),
//                    color = Color.LightGray,
                    thickness = 2.dp
                )
                CreateInfo()
                Button(
                    onClick = {
                        btnClickState.value = !btnClickState.value
                    },
                ) {
                    Text(text = "Portfolio",
                        textAlign = TextAlign.Center,
                        color = Color.White)
                }.apply { if (btnClickState.value) Content() else Box{} }
            }
        }
    }
}

@Preview(backgroundColor = 0xFFFFFF, showBackground = true)
@Composable
fun Content() {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(5.dp)
    ) {
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(3.dp),
            elevation = 4.dp,
            shape = RoundedCornerShape(corner = CornerSize(6.dp)),
            border = BorderStroke(2.dp, Color.LightGray)
        ) {
            Profile(data = listOf("Project 1", "Project 2", "Project 3", "Project 4", "Project 5", "Project 6"))
        }
    }
}

@Composable
private fun Profile(data: List<String>) {
    // 可當作RecyclerView
    LazyColumn {
        items(data) { item ->
            Card(modifier = Modifier
                .padding(13.dp)
                .fillMaxWidth(),
                shape = RectangleShape,
                elevation = 4.dp,
            ) {
                Row(modifier = Modifier
                    .padding(8.dp)
                    .background(MaterialTheme.colors.surface)
                    .padding(7.dp)
                ) {
                    CreateImageProfile(modifier = Modifier.size(100.dp))
                    Column(modifier = Modifier.padding(7.dp).align(Alignment.CenterVertically)) {
                        Text(text = item, fontWeight = FontWeight.Bold)
                        Text(text = "A great Project", style = MaterialTheme.typography.body2)
                    }
                }
            }
        }
    }
}

@Composable
private fun CreateInfo() {
    Column(modifier = Modifier.padding(5.dp)) {
        Text(modifier = Modifier.padding(top = 3.dp),
            text = "Name: Mai, Guang-Ting",
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.h5
        )
        Text(modifier = Modifier.padding(top = 3.dp),
            text = "Jetpack Compose Learning"
        )
        Text(modifier = Modifier.padding(top = 3.dp),
            text = "@themilesCompose"
        )
    }
}

@Composable
private fun CreateImageProfile(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .size(150.dp)
            .padding(5.dp),
        shape = CircleShape,
        elevation = 4.dp,
        border = BorderStroke(2.dp, Color.LightGray),
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
    ) {
        Image(modifier = modifier.size(135.dp),
            painter = painterResource(id = R.drawable.profile_image),
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BizCardTheme {
        CreateBizCard()
    }
}