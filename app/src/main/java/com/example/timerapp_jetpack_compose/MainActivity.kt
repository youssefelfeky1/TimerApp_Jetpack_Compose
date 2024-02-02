package com.example.timerapp_jetpack_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timerapp_jetpack_compose.ui.theme.TimerApp_Jetpack_ComposeTheme
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sign
import kotlin.math.sin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                color = Color(0xff101010),
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ){
                    Timer(totalTime = 100L/*sec*/ * 1000L/*to mSec*/,
                        handleColor = Color.Green,
                        inactiveBarColor = Color.DarkGray,
                        activeBarColor = Color(0xff37b900),
                        modifier = Modifier.size(200.dp)
                    )
                }

            }
        }
    }
}


@Composable
fun Timer(
    modifier:Modifier = Modifier,
    totalTime:Long,
    handleColor:Color,
    inactiveBarColor:Color,
    activeBarColor:Color,
    initialValue:Float =1f,
    strokeWidth: Dp = 5.dp,
){
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    var value by remember {
        mutableStateOf(initialValue)
    }

    var currentTime by remember {
        mutableStateOf(totalTime)
    }

    var isTimeRunning by remember {
        mutableStateOf(false)
    }
    
    LaunchedEffect(key1 = currentTime, key2 = isTimeRunning){
        if (currentTime>0 && isTimeRunning)
        {
            delay(100L)
            currentTime -=100L
            value = currentTime / totalTime.toFloat()
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .onSizeChanged {
                size = it
            }
    ){
        
        Canvas(modifier = modifier) {
            drawArc(
                color = inactiveBarColor,
                startAngle = -215f,
                sweepAngle = 250f,
                useCenter = false,
                size = Size(size.width.toFloat(),size.height.toFloat()),
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )

            drawArc(
                color = activeBarColor,
                startAngle = -215f,
                sweepAngle = 250f * value,
                useCenter = false,
                size = Size(size.width.toFloat(),size.height.toFloat()),
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )

            val center = Offset(size.width/2f,size.height/2f)
            val beta = (250f * value + 145f) * (PI/180f).toFloat()
            val r = size.width/2f
            val a = cos(beta) * r
            val b = sin(beta) * r

            drawPoints(
                listOf(Offset(center.x+a,center.y+b)),
                pointMode = PointMode.Points,
                color = handleColor,
                strokeWidth = (strokeWidth*3f).toPx(),
                cap = StrokeCap.Round
            )
        }

        Text(
            text = (currentTime / 1000f).toString(),
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
            )
        
        Button(
            modifier = Modifier.align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (! isTimeRunning || currentTime <= 0L) Color.Green else Color.Red
            ),
            onClick = {
                if(currentTime <= 0L){
                    currentTime = totalTime
                    isTimeRunning = true
                }
                else{
                    isTimeRunning = !isTimeRunning
                }

            }
        ) {
            Text(
                text = if (isTimeRunning && currentTime > 0L) "Stop"
                else if (! isTimeRunning &&currentTime >= 0L) "Start"
                else "Restart",
            )
        }

    }
}



