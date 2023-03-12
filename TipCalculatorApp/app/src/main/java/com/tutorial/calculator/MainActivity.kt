package com.tutorial.calculator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tutorial.calculator.components.InputField
import com.tutorial.calculator.ui.theme.TipCalculatorAppTheme
import com.tutorial.calculator.widgets.RoundIconButton
import kotlin.math.roundToInt

@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipCalculatorAppTheme {
                MyApp {
                    TipCalculator()
                }
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        content()
    }
}

@ExperimentalComposeUiApi
@Composable
fun TipCalculator() {
    Surface(
        modifier = Modifier
            .padding(12.dp),
    ) {
        Column { MainContent() }
    }
}

@Preview
@Composable
fun TopHeader(totalPerPerson: Double = 0.0) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(12.dp)
            // 3種都可以形成同樣效果
            .clip(shape = CircleShape.copy(all = CornerSize(12.dp))),
//            .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp))),
//        shape = RoundedCornerShape(corner = CornerSize(14.dp)),
        color = Color(0xFFE9D7F7)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val total = "%.2f".format(totalPerPerson)
            Text(text = "Total Per Person", style = MaterialTheme.typography.h4)
            Text(
                text = "$$total",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun MainContent() {
    BillForm { billAmt ->
        Log.d("AMT", "MainContent: $$billAmt")
    }
}

fun calculateTotalTip(totalBill: Double, tipPercent: Int): Double =
    if (totalBill > 1 && totalBill.toString().isNotEmpty())
        (totalBill * tipPercent) / 100
    else
        0.0

fun calculateTotalPerPerson(totalBill: Double, splitBy: Int, tipPercent: Int): Double {
    val bill = calculateTotalTip(totalBill, tipPercent = tipPercent) + totalBill
    return (bill / splitBy)
}

@ExperimentalComposeUiApi
@Composable
fun BillForm(modifier: Modifier = Modifier, onValueChange: (String) -> Unit = {}) {
    // 輸入內容狀態
    val totalBillState = remember { mutableStateOf("") }
    // 檢查是否有輸入內容
    val validState = remember(totalBillState.value) { totalBillState.value.trim().isNotBlank() }
    // 輸入鍵盤控制
    val keyboardController = LocalSoftwareKeyboardController.current

    val splitBy = remember { mutableStateOf(1) }

    var sliderPosition by remember { mutableStateOf(0f) }
    val totalTipAmt = remember { mutableStateOf(0.0) }
    val totalPerPerson = remember { mutableStateOf(0.0) }

    val tipPercentage = (sliderPosition * 100).roundToInt()

    Surface(
        modifier = modifier
            .padding(2.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(1.dp, Color.LightGray),
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            TopHeader(totalPerPerson = totalPerPerson.value)

            InputField(
                valueState = totalBillState,
                labelId = "Enter Bill",
                enable = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    // 當點擊輸入完成時，檢查是否有輸入內容，沒有就不隱藏鍵盤
                    if (!validState) return@KeyboardActions
                    onValueChange(totalBillState.value.trim())
                    keyboardController?.hide()
                }
            )
            if (validState) {
                Row(modifier = Modifier.padding(3.dp), horizontalArrangement = Arrangement.Start) {
                    Text(
                        text = "Split",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )

                    Spacer(modifier = Modifier.width(120.dp))

                    Row(
                        modifier = Modifier.padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        RoundIconButton(imageVector = Icons.Default.Remove, onClick = {
                            Log.d("Icon", "BillForm: Removed")
                            splitBy.value = if (splitBy.value > 1) splitBy.value - 1 else  1
                            totalPerPerson.value = calculateTotalPerPerson(
                                totalBill = totalBillState.value.toDouble(),
                                splitBy = splitBy.value,
                                tipPercent = tipPercentage)
                        })

                        Text(
                            text = "${splitBy.value}",
                            modifier = Modifier
                                .padding(horizontal = 9.dp)
                                .align(alignment = Alignment.CenterVertically)
                        )

                        RoundIconButton(imageVector = Icons.Default.Add, onClick = {
                            Log.d("Icon", "BillForm: Add")
                            splitBy.value += 1
                            totalPerPerson.value = calculateTotalPerPerson(
                                totalBill = totalBillState.value.toDouble(),
                                splitBy = splitBy.value,
                                tipPercent = tipPercentage)
                        })
                    }
                }
                Row(
                    modifier = Modifier.padding(horizontal = 3.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Tip",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )

                    Spacer(modifier = Modifier.width(169.dp))

                    Text(
                        text = "$${totalTipAmt.value}",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )

                }

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "$tipPercentage %")

                    Spacer(modifier = Modifier.height(14.dp))

                    Slider(value = sliderPosition, onValueChange = { newVal ->
                        sliderPosition = newVal
                        totalTipAmt.value = calculateTotalTip(
                            totalBill = totalBillState.value.toDouble(),
                            tipPercent = tipPercentage
                        )

                        totalPerPerson.value = calculateTotalPerPerson(
                            totalBill = totalBillState.value.toDouble(),
                            splitBy = splitBy.value,
                            tipPercent = tipPercentage
                        )
                        Log.d("Slider", "Total Bill-->: ${"%.2f".format(totalTipAmt.value)}")
                    }, modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                        steps = 5,
                        onValueChangeFinished = {
                            Log.d("Finished", "BillForm: $tipPercentage")
                            // This is were the calculations should happen!
                        })
                }
            } else {
                Box {}
            }
        }
    }
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun DefaultPreview() {
    TipCalculatorAppTheme {
        MyApp {
            TipCalculator()
        }
    }
}