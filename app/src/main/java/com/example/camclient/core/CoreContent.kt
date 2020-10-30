/** @module CoreContent
 *  @since 2020.10.30, 03:27
 *  @changed 2020.10.31, 01:36
 */

package com.example.camclient.core

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import java.util.ArrayList
import java.util.HashMap
import org.json.JSONObject

import com.example.camclient.config.RouteIds
import com.example.camclient.config.Routes

import com.example.camclient.helpers.Requestor
import kotlinx.coroutines.runBlocking

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object CoreContent {

    private const val TAG: String = "DEBUG:CoreContent"

    private lateinit var context: Context
    private lateinit var queue: RequestQueue

    /**
     * A dummy item representing a piece of content.
     */
    data class ImageItem(val id: String, val content: String, val details: String) {
        override fun toString(): String = content
    }

    /**
     * An array of sample (dummy) items.
     */
    val ITEMS: MutableList<ImageItem> = ArrayList()

    /**
     * A map of sample (dummy) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, ImageItem> = HashMap()

    private val COUNT = 25

    init {
        // Toast.makeText(this, "Init!", Toast.LENGTH_LONG).show()
        Log.d(TAG, "init")
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(createImageItem(i))
        }
    }

    fun start(context: Context) {
        this.context = context
        Requestor.start(context)
        this.requestData()
        // this.requestDataAsync() // Failed!
        // this.requestDataRaw()
        // NOTE: Test for processing async functions
        // Requestor.runTestAsyncResult()
    }

    private fun requestData() {
        val method = Request.Method.GET
        val url = Routes.getRoute(RouteIds.Recent)
        Log.d(TAG, "requestData: start: url: $url")
        try {
            val result = Requestor.fetchCallback(method, url, JSONObject(),
                { result ->
                    Log.d(TAG, "requestData: success: $result")
                    // TODO: Populate received data...
                }
            )
        }
        catch (ex: Exception) {
            val message = ex.message
            val stacktrace = ex.getStackTrace().joinToString("\n")
            Log.d(TAG, "requestData: error: $message / Stacktrace: $stacktrace")
        }
        Log.d(TAG, "requestData: after coroutine")
    }

    private fun requestDataAsync() { // NOTE: Example!
        val method = Request.Method.GET
        val url = Routes.getRoute(RouteIds.Recent)
        Log.d(TAG, "requestData: start: url: $url")
        runBlocking {
            try {
                // val result = Requestor.fetch(method, url, null)
                val defer = Requestor.fetchDeferred(method, url, null) // NOTE: This is not work: seems to problems with using queue under coroutines
                val result = defer.await()
                Log.d(TAG, "requestData: success: $result")
            }
            catch (ex: Exception) {
                val message = ex.message
                val stacktrace = ex.getStackTrace().joinToString("\n")
                Log.d(TAG, "requestData: error: $message / Stacktrace: $stacktrace")
            }
        }
        Log.d(TAG, "requestData: after coroutine")
    }

    private fun addItem(item: ImageItem) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    private fun createImageItem(position: Int): ImageItem {
        return ImageItem(position.toString(), "Item " + position, makeDetails(position))
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0..position - 1) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

}
