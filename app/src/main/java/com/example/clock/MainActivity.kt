package com.example.clock

import android.graphics.Paint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val milliseconds = remember {
                    System.currentTimeMillis()
                }
                var seconds by remember {
                    mutableStateOf((milliseconds / 1000f) % 60f)
                }
                var minutes by remember {
                    mutableStateOf(((milliseconds / 1000f) / 60) % 60f)
                }
                var hours by remember {
                    mutableStateOf((milliseconds / 1000f) / 3600f + 2f)
                }
                LaunchedEffect(key1 = seconds) {
                    delay(1000L)
                    minutes += 1f / 60f
                    hours += 1f / (60f * 12f)
                    seconds += 1f
                }
                Clock(
                    seconds = seconds,
                    minutes = minutes,
                    hours = hours
                )
            }
        }
    }
}

@Composable
fun Clock(
    seconds: Float = 0f,
    minutes: Float = 0f,
    hours: Float = 0f,
    radius: Dp = 100.dp
) {
    Canvas(modifier = Modifier.size(radius * 2f)) {
        drawContext.canvas.nativeCanvas.apply {
            drawCircle(
                center.x,
                center.y,
                radius.toPx(),
                Paint().apply {
                    color = android.graphics.Color.WHITE
                    setShadowLayer(
                        50f,
                        0f,
                        0f,
                        android.graphics.Color.argb(50, 0, 0, 0)
                    )
                }
            )
        }

        // Lines
        for (i in 0..59) {
            val angleInRad = i * (360f / 60f) * (PI.toFloat() / 180f)
            val lineLength = if (i % 5 == 0) 20.dp.toPx() else 15.dp.toPx()
            val strokeWidth = if (i % 5 == 0) 1.dp.toPx() else 0.5.dp.toPx()
            val color = if(i % 5 == 0) androidx.compose.ui.graphics.Color.DarkGray else Color(0xFF606060)

            val lineStart = Offset(
                x = radius.toPx() * cos(angleInRad) + center.x,
                y = radius.toPx() * sin(angleInRad) + center.y
            )
            val lineEnd = Offset(
                x = (radius.toPx() - lineLength) * cos(angleInRad) + center.x,
                y = (radius.toPx() - lineLength) * sin(angleInRad) + center.y
            )
            drawLine(
                color = color,
                start = lineStart,
                end = lineEnd,
                strokeWidth = strokeWidth
            )
        }

        // Seconds
        rotate(degrees = seconds * (360f / 60f)) {
            drawLine(
                color = androidx.compose.ui.graphics.Color.Red,
                start = center,
                end = Offset(center.x, 20.dp.toPx()),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
        // Minutes
        rotate(degrees = minutes * (360f / 60f)) {
            drawLine(
                color = androidx.compose.ui.graphics.Color.Black,
                start = center,
                end = Offset(center.x, 20.dp.toPx()),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
        // Hours
        rotate(degrees = hours * (360f / 12f)) {
            drawLine(
                color = androidx.compose.ui.graphics.Color.Black,
                start = center,
                end = Offset(center.x, 35.dp.toPx()),
                strokeWidth = 4.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}