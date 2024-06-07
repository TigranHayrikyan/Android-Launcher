package am.bootech.erplauncher.activity

import am.bootech.erplauncher.R
import am.bootech.erplauncher.models.JsonRoot
import am.bootech.erplauncher.utils.SubFramesConverter
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import okhttp3.Response

class MainActivity : BaseActivity() {

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
        val dataUrl = "https://realschool.am/oo/wrap?id=f28a2f1e-0729-40d5-aa2f-169cec17babb-f16c5814-86a2-4091-a09a-7a98a1e94949"
        fetchData(dataUrl) {
            initMainView(it)
        }
    }

    private fun initMainView(response: Response) {
        val gson = Gson()
        val jsonRoot = gson.fromJson(response.body?.string(), JsonRoot::class.java)
        BaseActivity.jsonRoot = jsonRoot
        val rootLayout: LinearLayout = LinearLayout(applicationContext).apply {
            setPadding(20, 100, 20, 0)
            orientation = if (jsonRoot.Direction == "Ուղղահայաց") {
                LinearLayout.VERTICAL
            } else {
                LinearLayout.HORIZONTAL
            }
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }

        jsonRoot.type.collections.forEach { collection ->
            Button(applicationContext).apply {
                text = collection.name
                if (collection.id == "widget") {
                    jsonRoot.widget.forEachIndexed { index, uuid ->
                        SubFramesConverter.createViews(
                            this@MainActivity,
                            uuid,
                            rootLayout,
                            index
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
        }
        this@MainActivity.runOnUiThread {
            setContentView(rootLayout)
        }
    }
}