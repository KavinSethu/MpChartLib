package com.example.mpchartlib

import CustomData
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.ViewPortHandler

@SuppressLint("ViewConstructor")
class MyMarkerView(context: Context, layoutRes: Int) : MarkerView(context, layoutRes) {

    private val valueTv: TextView = findViewById(R.id.value_tv)
    private val dateTv: TextView = findViewById(R.id.date_tv)
    private val topArrow: ImageView = findViewById(R.id.top_arrow)
    private val bottomArrow: ImageView = findViewById(R.id.bottom_arrow)

    override fun draw(canvas: Canvas, posX: Float, posY: Float) {
        val x = -width / 2f
        var y = -height - 8f

        topArrow.isVisible = false
        bottomArrow.isVisible = true

        if (posY < canvas.height / 2f) {
            y = 8f
            topArrow.isVisible = true
            bottomArrow.isVisible = false
        }

        offset = MPPointF(x, y)
        invalidate()
        super.draw(canvas, posX, posY)
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        if (e == null) return

        val customData = e.data as? CustomData

        if (customData != null) {
            valueTv.text = "${customData?.value} ${customData.unit}"
            dateTv.text = "${customData.date}"
        } else {
            valueTv.text = ""
            dateTv.text =  ""
        }

        super.refreshContent(e, highlight)
    }

    companion object {
        fun create(context: Context, layoutRes: Int): MyMarkerView {
            val markerView = MyMarkerView(context, layoutRes)
            markerView.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            return markerView
        }
    }
}