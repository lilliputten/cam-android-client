package com.example.masterdetail.core

import android.app.DownloadManager
import android.content.Context
import android.os.Build
import android.util.Base64
import android.util.Log
import com.android.volley.AuthFailureError
import java.util.ArrayList
import java.util.HashMap
import android.widget.Toast
//import com.android.volley
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.Request
import com.android.volley.Response
import org.json.JSONObject

import com.example.masterdetail.config.CoreUtils
import com.example.masterdetail.config.Params
import com.example.masterdetail.config.RouteIds
import com.example.masterdetail.config.Routes

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object CoreContent {

    private const val TAG: String = "DEBUG:CoreContent"

    private lateinit var context: Context

    /**
     * An array of sample (dummy) items.
     */
    val ITEMS: MutableList<DummyItem> = ArrayList()

    /**
     * A map of sample (dummy) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, DummyItem> = HashMap()

    private val COUNT = 25

    init {
        // Toast.makeText(this, "Init!", Toast.LENGTH_LONG).show()
        Log.d(TAG, "init")
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(createDummyItem(i))
        }
    }

    fun start(context: Context) {
        this.context = context
        requestData()
    }

    private fun requestData() {
        val url = Routes.getRoute(RouteIds.Recent)
        Log.d(TAG, "requestData: url: $url")
        val queue = Volley.newRequestQueue(this.context)
        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, JSONObject(),
            Response.Listener { response ->
                Toast.makeText(this.context, "Request success!", Toast.LENGTH_LONG).show()
                Log.d(TAG, "requestData: response: %s".format(response.toString()))
            },
            Response.ErrorListener { error -> // Handle error
                val errorStr = error.toString()
                Toast.makeText(this.context, "Request error: $errorStr", Toast.LENGTH_LONG).show()
                Log.d(TAG, "requestData: error: $errorStr")
            }
        ) {
            // Add authorization header (like 'Authorization: Basic Z3Vlc3Q6MTIz')
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>() // It works!
                val cred:String = String.format("%s:%s", "guest", "123")
                val auth = "Basic " + Base64.encodeToString(cred.toByteArray(), Base64.NO_WRAP)
                Log.d(TAG, "requestData: auth: $auth")
                headers["Authorization"] = auth
                return headers
            }
        }
        // Access the RequestQueue through your singleton class.
        // MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
        queue.add(jsonObjectRequest)
    }

    private fun addItem(item: DummyItem) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    private fun createDummyItem(position: Int): DummyItem {
        return DummyItem(position.toString(), "Item " + position, makeDetails(position))
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0..position - 1) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

    /**
     * A dummy item representing a piece of content.
     */
    data class DummyItem(val id: String, val content: String, val details: String) {
        override fun toString(): String = content
    }
}
