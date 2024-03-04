package com.example.mpchartlib

import CustomData
import CustomXAxisValueFormatter
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.mpchartlib.ui.theme.MpChartLibTheme
import com.example.mpchartlib.ui.theme.thmeme
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

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

                    val xData = listOf(1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 11f ,12f)
                    val yData = listOf(50f, 30f, 40f, 35f, 50f, 45f, 60f, 70f, 80f, 40f, 70f)


                    val customDataList = listOf(
                        CustomData(112f, "mmHg", "4 March"),
                        CustomData(120f, "mmHg", "5 March"),
                        CustomData(105f, "mmHg", "6 March"),
                        CustomData(118f, "mmHg", "7 March"),
                        CustomData(125f, "mmHg", "8 March"),
                        CustomData(108f, "mmHg", "9 March")
                    )

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .height(200.dp)) {

                        LineGraph(xData = xData, yData = yData, dataLabel = "", customDataList = customDataList)
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
    customDataList: List<CustomData>,
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

            //input data
            val chart = LineChart(context)  // Initialise the chart

            //convert the given list into entried list
            val entries = customDataList.mapIndexed { index, customData ->
                Entry(index.toFloat(), customData.value, customData)
            }


            //create data set state
            val dataSet = LineDataSet(entries, dataLabel).apply {
                // Here we apply styling to the dataset
                color = lineColor.toArgb()
                lineWidth = 2f
                setDrawValues(drawValues)

                setDrawCircles(drawMarkers)
                setCircleColor(circleColor.toArgb())
                setCircleHoleColor(circleHoleColor.toArgb())
                circleRadius = 3f
                setCircleHoleRadius(2f)

                setDrawFilled(drawFilled)
                setDrawHighlightIndicators(false)
                fillAlpha = fillAlpha
            }
            chart.data = LineData(dataSet)




            // Enable touch gestures
            chart.setTouchEnabled(true)
            chart.setPinchZoom(false)
            chart.isDragEnabled = true
            chart.isScaleXEnabled = false
            chart.isScaleYEnabled = false




            // Create an instance of your custom formatter
            val customXAxisValueFormatter = CustomXAxisValueFormatter(entries)


            // Customize x-axis
            val xAxis = chart.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.setDrawLabels(true)
            xAxis.isGranularityEnabled = true
            xAxis.labelCount = entries.size
            xAxis.textColor = Color.Black.toArgb()
            xAxis.textSize = 7f
            xAxis.setDrawAxisLine(false) //Remove Border for the axis
            xAxis.setAvoidFirstLastClipping(true) //axis initial and end lable pading
            xAxis.valueFormatter = customXAxisValueFormatter

            // Customize y-axis
            val leftAxis = chart.axisLeft
            leftAxis.setDrawGridLines(false)
            leftAxis.textColor = Color.Black.toArgb()
            leftAxis.setDrawAxisLine(false)

            // Disable right y-axis
            val rightAxis = chart.axisRight
            rightAxis.isEnabled = false

            // Set chart description
            chart.description.isEnabled = false







            // Listener for chart touch events
            chart.onChartGestureListener = object : OnChartGestureListener {
                override fun onChartGestureStart(
                    me: MotionEvent?,
                    lastPerformedGesture: ChartTouchListener.ChartGesture?
                ) {
                    chart.highlightValues(null)
                    chart.marker = null
                }

                override fun onChartGestureEnd(
                    me: MotionEvent?,
                    lastPerformedGesture: ChartTouchListener.ChartGesture?
                ) {
                }

                override fun onChartLongPressed(me: MotionEvent?) {
                }

                override fun onChartDoubleTapped(me: MotionEvent?) {
                }

                override fun onChartSingleTapped(me: MotionEvent?) {
                }

                override fun onChartFling(
                    me1: MotionEvent?,
                    me2: MotionEvent?,
                    velocityX: Float,
                    velocityY: Float
                ) {
                }

                override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float) {
                }

                override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {
                }
            }

            // Listener for value selected
            chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    e?.let {
                        val x = e.x.toInt()
                        val y = e.y

                        val markerView = MyMarkerView.create(context, R.xml.custom_marker_view)
                        chart.marker = markerView
                    }
                }

                override fun onNothingSelected() {
                }
            })

            val markerView = MyMarkerView.create(context, R.xml.custom_marker_view)
            chart.marker = markerView


            // Refresh and return the chart
            chart.invalidate()

            //disable lengeds
            chart.legend.isEnabled = false

            //visible items range
            chart.setVisibleXRangeMaximum(4f)

            // Disable highlight on drag
            chart.isHighlightPerDragEnabled = false


            chart
        }
    )
}