package am.bootech.erplauncher.utils

import android.content.res.Resources

fun Int.dp(): Int {
    val density = Resources.getSystem().displayMetrics.density
    return (this * density + 0.5f).toInt()
}