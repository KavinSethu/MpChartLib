import android.text.Html
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter

class CustomXAxisValueFormatter(private val entries: List<Entry>) : ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val index = value.toInt()
        if (index >= 0 && index < entries.size) {
            val customData = entries[index].data as? CustomData
            customData?.let {
                // Format the label as "Value\nDate" using HTML
                return Html.fromHtml("${it.value}<br>${it.date}", Html.FROM_HTML_MODE_LEGACY).toString()
            }
        }
        return ""
    }
}

data class CustomData(val value: Float, val unit: String, val date: String)