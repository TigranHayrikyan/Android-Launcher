package am.bootech.erplauncher.utils

import am.bootech.erplauncher.R
import am.bootech.erplauncher.models.JsonRoot
import android.app.Activity
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson

object SubFramesConverter {

    private const val SUFFIX = "https://realschool.am/oo/wrap?id="
    private val gson = Gson()

    fun createViews(
        activity: Activity,
        uuid: String,
        rootLayout: LinearLayout,
    ) {
        GetRequest().fetchData(SUFFIX + uuid) {
            val jsonRoot = gson.fromJson(it.body?.string(), JsonRoot::class.java)
            convertToView(activity, jsonRoot, rootLayout, null)
        }
        activity.runOnUiThread {
            activity.setContentView(rootLayout)
        }
    }

    private fun convertToView(
        activity: Activity,
        jsonRoot: JsonRoot,
        rootLayout: LinearLayout,
        subLayout: LinearLayout?,
    ) {
        activity.runOnUiThread {
            val subLinear = LinearLayout(activity).apply {
                orientation = if (jsonRoot.direction == DirectionUUIDs.DIRECTION_VERTICAL) {
                    LinearLayout.VERTICAL
                } else {
                    LinearLayout.HORIZONTAL
                }
                layoutParams = LinearLayout.LayoutParams(
                    (jsonRoot.width.toInt()).dp(),
                    (jsonRoot.height.toInt()).dp()
                )
                if (jsonRoot.backgroundColor.isNotEmpty()) {
                    val color = Color.parseColor(jsonRoot.backgroundColor)
                    setBackgroundColor(color)
                }
                setPadding(16, 16, 16, 16)
            }
            if (jsonRoot.subframes.isNotEmpty()) {
                jsonRoot.subframes.forEach { uuid ->
                    GetRequest().fetchData(SUFFIX + uuid) {
                        val childJsonRoot = gson.fromJson(it.body?.string(), JsonRoot::class.java)
                        convertToView(activity, childJsonRoot, rootLayout, subLinear)
                    }
                }
            }
            addWidgets(jsonRoot, activity, subLinear)
            if (subLayout != null) {
                subLayout.addView(subLinear)
            } else {
                rootLayout.addView(subLinear)
            }
        }
    }

    fun addWidgets(jsonRoot: JsonRoot, activity: Activity, view: LinearLayout) {
        jsonRoot.widgets.forEach { uuid ->
            GetRequest().fetchData(SUFFIX + uuid) { widgetResponse ->
                val widget = gson.fromJson(
                    widgetResponse.body?.string(),
                    JsonRoot::class.java
                )
                Log.d("TAG", "addWidgets: ${widget.kind}")
                activity.runOnUiThread {
                    addWidgetByType(widget, view, activity)
                }
            }
        }
    }

    private fun addWidgetByType(
        widget: JsonRoot,
        view: LinearLayout,
        activity: Activity,
    ) {
        var parsedView = View(activity)
        when (widget.kind) {
            KindUUIDs.IMAGE -> {
                parsedView = ImageView(activity).apply {
                    setImageResource(R.drawable.ic_launcher_foreground)
                }
            }

            KindUUIDs.TEXT -> {
                parsedView = TextView(activity).apply {
                    text = widget.description
                }
            }

            KindUUIDs.LIST -> {
                parsedView = TextView(activity).apply {
                    text = widget.description
                    textSize = getTextSize(widget)
                }
            }

            KindUUIDs.BUTTON -> {
                parsedView = Button(activity).apply {
                    text = widget.name
                    gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
                }
            }
        }
        view.addView(parsedView)
    }

    private fun getTextSize(widget: JsonRoot): Float {
        return if (widget.fontSize != null && widget.fontSize != "") {
            widget.fontSize.toFloat()
        } else {
            18F
        }
    }
}