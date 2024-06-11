package am.bootech.erplauncher.activity

import am.bootech.erplauncher.R
import am.bootech.erplauncher.models.JsonRoot
import am.bootech.erplauncher.utils.DirectionUUIDs
import am.bootech.erplauncher.utils.GetRequest
import am.bootech.erplauncher.utils.SubFramesConverter
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Response

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val dataUrl =
            "https://realschool.am/oo/wrap?id=40b89749-107c-4ebc-91df-5fc7150af39a-59186cfc-5d5c-4b8d-9b24-6b818804a642"
        GetRequest().fetchData(dataUrl) {
            initMainView(it)
        }
    }

    private fun initMainView(response: Response) {
        val gson = Gson()
        val jsonRoot = gson.fromJson(response.body?.string(), JsonRoot::class.java)
        val rootLayout: LinearLayout = LinearLayout(applicationContext).apply {
            setPadding(20, 100, 20, 0)
            orientation = if (jsonRoot.direction == DirectionUUIDs.DIRECTION_VERTICAL) {
                LinearLayout.VERTICAL
            } else {
                LinearLayout.HORIZONTAL
            }
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            if (jsonRoot.backgroundColor.isNotEmpty()) {
                val color = Color.parseColor(jsonRoot.backgroundColor)
                setBackgroundColor(color)
            }
        }

        jsonRoot.type.collections.forEach { collection ->
            if (collection.id == "subframes") {
                jsonRoot.subframes.forEach { uuid ->
                    SubFramesConverter.createViews(
                        this@MainActivity,
                        uuid,
                        rootLayout
                    )
                }
            } else if (collection.id == "widgets") {
                if (jsonRoot.widgets.isNotEmpty()) {
                    SubFramesConverter.addWidgets(
                        jsonRoot,
                        this@MainActivity,
                        rootLayout,
                    )
                }
            }
        }
        this@MainActivity.runOnUiThread {
            setContentView(rootLayout)
        }
    }
}