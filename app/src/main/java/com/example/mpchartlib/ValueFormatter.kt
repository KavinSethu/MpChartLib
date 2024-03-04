import android.graphics.Canvas
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler


class CustomXAxisValueFormatter(private val entries: List<Entry>) : ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val index = value.toInt()
        if (index >= 0 && index < entries.size) {
            val customData = entries[index].data as? CustomData
            customData?.let {
                return "${removeDecimalIfInteger(customData.value.toString())} \n ${customData.date}"
            }
        }
        return ""
    }
}

fun removeDecimalIfInteger(value: String): String {
    return if (value.endsWith(".0")) {
        value.substring(0, value.length - 2)  // Remove ".0" if it's at the end
    } else {
        value  // Keep the string as is
    }
}

data class CustomData(val value: Float, val unit: String, val date: String)


class CustomXAxisRenderer(viewPortHandler: ViewPortHandler?, xAxis: XAxis?, trans: Transformer?) :
    XAxisRenderer(viewPortHandler, xAxis, trans) {
    override fun drawLabel(
        c: Canvas?,
        formattedLabel: String,
        x: Float,
        y: Float,
        anchor: MPPointF?,
        angleDegrees: Float
    ) {
        val line = formattedLabel.split("\n".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        Utils.drawXAxisValue(
            c,
            line[0], x, y, mAxisLabelPaint, anchor, angleDegrees
        )
        Utils.drawXAxisValue(
            c,
            line[1],
            x,
            y + mAxisLabelPaint.textSize,
            mAxisLabelPaint,
            anchor,
            angleDegrees
        )
    }
}