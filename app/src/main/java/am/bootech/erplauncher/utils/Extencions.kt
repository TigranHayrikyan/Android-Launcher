package am.bootech.erplauncher.utils

import am.bootech.erplauncher.models.JsonRoot
import android.content.res.Resources
import android.view.Gravity
import android.view.View

fun Int.dp(): Int {
    val density = Resources.getSystem().displayMetrics.density
    return (this * density + 0.5f).toInt()
}
 fun View.setPreparedGravity(widget: JsonRoot?): Int {
    return when (widget?.positioning) {
        PositionUUIDs.TOP_START -> Gravity.TOP or Gravity.START
        PositionUUIDs.TOP_CENTER -> Gravity.TOP or Gravity.CENTER_HORIZONTAL
        PositionUUIDs.TOP_END -> Gravity.TOP or Gravity.END
        PositionUUIDs.CENTER_START -> Gravity.CENTER_VERTICAL or Gravity.START
        PositionUUIDs.CENTER_CENTER -> Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL
        PositionUUIDs.CENTER_END -> Gravity.CENTER_VERTICAL or Gravity.END
        PositionUUIDs.BOTTOM_START -> Gravity.BOTTOM or Gravity.START
        PositionUUIDs.BOTTOM_CENTER -> Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
        PositionUUIDs.BOTTOM_END -> Gravity.BOTTOM or Gravity.END
        else -> {
            Gravity.NO_GRAVITY
        }
    }
}