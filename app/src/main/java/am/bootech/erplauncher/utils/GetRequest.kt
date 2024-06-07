package am.bootech.erplauncher.utils

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class GetRequest {
    fun fetchData(url: String, actionSuccess: (Response) -> Unit) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    actionSuccess.invoke(response)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
            }
        })
    }
}