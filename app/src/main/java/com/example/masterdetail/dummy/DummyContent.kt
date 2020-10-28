package com.example.masterdetail.dummy

import android.app.DownloadManager
import android.content.Context
import android.util.Log
import java.util.ArrayList
import java.util.HashMap
//import android.widget.Toast
//import com.android.volley
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.Request
import com.android.volley.Response

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object DummyContent {

    private const val TAG: String = "DummyContent"

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
        Log.d(TAG, "DummyContent test log: init")
        // this.context = context.applicationContext
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(createDummyItem(i))
        }
    }

    fun start(con: Context) {
        this.context = con
        requestData()
    }

    private fun requestData() {
        // TODO 2020.10.28, 06:06 -- Can't connect to localhost?
        val url = "http://localhost:5000/api/images/recent"
        // val url = "https://cam.lilliputten.ru/api/images/recent"
        Log.d(TAG, "requestData: url: $url")
        // private lateinit var context: Context
        val queue = Volley.newRequestQueue(this.context)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                // textView.text = "Response: %s".format(response.toString())
                // Toast.makeText(this, "Success!", Toast.LENGTH_LONG).show()
                Log.d(TAG, "requestData: response: %s".format(response.toString()))
            },
            Response.ErrorListener { error ->
                // TODO: Handle error
                // Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show()
                Log.d(TAG, "requestData: error: %s".format(error.toString()))
            }
        )
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
