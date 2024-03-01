package com.example.mpchartlib

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mpchartlib.ui.theme.MpChartLibTheme
import androidx.compose.ui.viewinterop.AndroidView
import com.example.mpchartlib.ui.theme.thmeme
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MpChartLibTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val xData = listOf(1f, 2f, 3f, 4f, 5f, 6f, 7f)
                    val yData = listOf(20f, 30f, 40f, 35f, 50f, 45f, 60f)

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .height(200.dp)) {

                        LineGraph(xData = xData, yData = yData, dataLabel = "data")
                    }
                }
            }
        }
    }
}

@Composable
fun LineGraph(
    xData: List<Float>,
    yData: List<Float>,
    dataLabel: String,
    lineColor: Color = thmeme,
    fillColor: Color = thmeme,
    circleHoleColor: Color = Color.White,
    circleColor: Color = thmeme,
    drawValues: Boolean = false,
    drawMarkers: Boolean = true,
    drawFilled: Boolean = false,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier
            .height(200.dp)
            .fillMaxWidth(),
        factory = { context ->
            val chart = LineChart(context)  // Initialise the chart
            val entries: List<Entry> =
                xData.zip(yData) { x, y -> Entry(x, y) }  // Convert the x and y data into entries
            val dataSet = LineDataSet(entries, dataLabel).apply {
                // Here we apply styling to the dataset
                color = lineColor.toArgb()
                setDrawValues(drawValues)
                setDrawCircles(drawMarkers)
                setCircleColor(circleColor.toArgb())
                setCircleHoleColor(circleHoleColor.toArgb())
                setDrawFilled(drawFilled)
                fillAlpha = fillAlpha
            }
            chart.data = LineData(dataSet)

            // Enable touch gestures
            chart.setTouchEnabled(true)
            chart.isDragEnabled = true
            chart.isScaleXEnabled = true
            chart.isScaleYEnabled = false


            //Remove Border
            // Customize x-axis
            val xAxis = chart.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.setDrawLabels(true)
            xAxis.textColor = Color.Black.toArgb()

            // Customize y-axis
            val leftAxis = chart.axisLeft
            leftAxis.setDrawGridLines(false)
            leftAxis.textColor = Color.Black.toArgb()

            // Disable right y-axis
            val rightAxis = chart.axisRight
            rightAxis.isEnabled = false

            // Set chart description
            chart.description.isEnabled = false





            // Refresh and return the chart
            chart.invalidate()
            chart
        }
    )
}