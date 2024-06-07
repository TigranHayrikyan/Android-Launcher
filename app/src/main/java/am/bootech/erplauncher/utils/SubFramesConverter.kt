package am.bootech.erplauncher.utils

import am.bootech.erplauncher.R
import am.bootech.erplauncher.models.JsonRoot
import android.app.Activity
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.gson.Gson

object SubFramesConverter {

    private const val suffix = "https://realschool.am/oo/wrap?id="
    private val gson = Gson()

    fun createViews(
        activity: Activity,
        uuid: String,
        rootLayout: LinearLayout,
        index: Int
    ) {
        GetRequest().fetchData(suffix + uuid) {
            val jsonRoot = gson.fromJson(it.body?.string(), JsonRoot::class.java)
            convertToView(activity, jsonRoot, rootLayout, null, index)
        }
        activity.runOnUiThread {
            activity.setContentView(rootLayout)
        }
    }

    private fun <T> convertToView(
        activity: Activity,
        jsonRoot: JsonRoot,
        rootLayout: LinearLayout,
        layout: T?,
        index: Int
    ) {
        activity.runOnUiThread {
            val frameLayout = FrameLayout(activity).apply {
                layoutParams = FrameLayout.LayoutParams(
                    (jsonRoot.width.toInt()).dp(),
                    (jsonRoot.height.toInt()).dp()
                )
                if (jsonRoot.backgroundColor.isNotEmpty()) {
                    val color = Color.parseColor(jsonRoot.backgroundColor)
                    setBackgroundColor(color)
                }
                setPadding(16, 16, 16, 16)
            }
            addWidgets(jsonRoot, activity, frameLayout)
            if (jsonRoot.subframes.isNotEmpty()) {
                jsonRoot.subframes.forEachIndexed { index, uuid ->
                    GetRequest().fetchData(suffix + uuid) {
                        val childJsonRoot = gson.fromJson(it.body?.string(), JsonRoot::class.java)
                        convertToView(activity, childJsonRoot, rootLayout, frameLayout, index)
                    }
                }
            }
            if (layout != null) {
                (layout as FrameLayout).addView(frameLayout)
            } else {
                rootLayout.addView(frameLayout)
            }
        }
    }

    fun addWidgets(jsonRoot: JsonRoot, activity: Activity, view: ViewGroup) {
        jsonRoot.widgets.forEach { uuid ->
            GetRequest().fetchData(suffix + uuid) { widgetResponse ->
                val widget = gson.fromJson(
                    widgetResponse.body?.string(),
                    JsonRoot::class.java
                )
                activity.runOnUiThread {
                    if (widget.name == "Նկար") {
                        view.addView(
                            ImageView(activity).apply {
                                layoutParams = FrameLayout.LayoutParams(
                                    (jsonRoot.width.toInt() / 4).dp(),
                                    (jsonRoot.height.toInt() / 4).dp()
                                )
                            }
                        )
                    } else {
                        view.addView(
                            Button(activity).apply {
                                text = widget?.name
                                layoutParams = FrameLayout.LayoutParams(
                                    (jsonRoot.width.toInt() / 4).dp(),
                                    (jsonRoot.height.toInt() / 4).dp()
                                ).apply {
                                    gravity = setPreparedGravity(widget)
                                }
                            }
                        )
                    }
                }

            }
        }
    }

    private fun setPreparedGravity(widget: JsonRoot?): Int {
        return when (widget?.Positioning) {
            "1" -> Gravity.TOP or Gravity.START
            "2" -> Gravity.TOP or Gravity.CENTER_HORIZONTAL
            "3" -> Gravity.TOP or Gravity.END
            "4" -> Gravity.CENTER_VERTICAL or Gravity.START
            "5" -> Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL
            "6" -> Gravity.CENTER_VERTICAL or Gravity.END
            "7" -> Gravity.BOTTOM or Gravity.START
            "8" -> Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
            "9" -> Gravity.BOTTOM or Gravity.END
            else -> {
                Gravity.NO_GRAVITY
            }
        }
    }

    private fun getColor(index: Int): Int {
        return when (index) {
            1 -> R.color.white
            2 -> R.color.red
            3 -> R.color.green
            else -> R.color.blue
        }
    }
}