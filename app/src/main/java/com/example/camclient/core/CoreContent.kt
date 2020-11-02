/** @module CoreContent
 *  @since 2020.10.30, 03:27
 *  @changed 2020.11.02, 03:06
 *
 *  TODO:
 *  - Cache images list
 */

package com.example.camclient.core

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.example.camclient.config.RouteIds
import com.example.camclient.config.Routes
import com.example.camclient.helpers.Requestor
import java.util.ArrayList
import java.util.HashMap
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject

/**
 * Images list data helpers..
 */
object CoreContent {

    private const val TAG: String = "DEBUG:CoreContent"

    private var inited: Boolean = false

    private lateinit var context: Context
    private lateinit var updateCallback: () -> Unit
    // private lateinit var queue: RequestQueue

    /**
     * Images list item type.
     * Item sample data: {"id":"xxx","ip":"81.195.31.186","timestamp":"2020.10.23-00:59"}
     */
    data class ImageItem(
        val id: String,
        val ip: String,
        val timestamp: String,
    ) {
        override fun toString(): String = "${this.timestamp} (${this.ip})"
    }

    /**
     * List of image items.
     */
    val ITEMS: MutableList<ImageItem> = ArrayList()

    /**
     * A map of image items, by ID.
     */
    val ITEM_MAP: MutableMap<String, ImageItem> = HashMap()

    // private val COUNT = 25

    init {
        // Toast.makeText(this.context, "Init!", Toast.LENGTH_LONG).show()
        Log.d(TAG, "init")
        // val itemObject = ImageItem("testId", "testIp", "testTimestamp")
        // this.addItem(itemObject)
        // Add some sample items.
        // for (i in 1..COUNT) {
        //     addItem(createImageItem(i))
        // }
    }

    fun start(context: Context, updateCallback: () -> Unit) {
        this.context = context
        this.updateCallback = updateCallback
        Log.d(TAG, "start: context: ${this.context}")
        Requestor.start(context)
        if (!this.inited) {
            this.inited = true
            this.requestData()
            // this.requestDataAsync() // Failed!
            // NOTE: Test for processing async functions
            // Requestor.runTestAsyncResult()
        }
        else {
            this.updateCallback()
        }
    }

    private fun setImagesList(images: JSONArray?) {
        try {
            this.ITEMS.clear()
            this.ITEM_MAP.clear()
            if (images is JSONArray) {
                // throw Exception("Images list is required!")
                // val images = data["images"] as JSONArray
                val itemsCount = images.length()
                Log.d(TAG, "setImagesList: ($itemsCount) $images")
                for (i in 0 until itemsCount) {
                    val item = images[i] as JSONObject
                    // item sample data: {"id":"xxx","ip":"81.195.31.186","timestamp":"2020.10.23-00:59"}
                    val id = item["id"] as String
                    val ip = item["ip"] as String
                    val timestamp = item["timestamp"] as String
                    // val test = item["test"] // Ivalid key error test (must be catched)
                    Log.d(TAG, "setImagesList: addItem ($i) $item")
                    val itemObject = ImageItem(id, ip, timestamp)
                    this.addItem(itemObject)
                }
            }
            Log.d(TAG, "setImagesList: calling updateCallback with $this.ITEMS")
            if (!(this::updateCallback.isInitialized)) {
                throw Exception("`updateCallback` must be initialized!")
            }
            this.updateCallback()
        }
        catch (ex: Exception) {
            val message = ex.message
            val stacktrace = ex.getStackTrace().joinToString("\n")
            Log.d(TAG, "setImagesList: error: $message / Stacktrace: $stacktrace")
            Toast.makeText(this.context, "setImagesList: Error: $message", Toast.LENGTH_LONG).show()
        }
    }

    private fun requestData() {
        val method = Request.Method.GET
        val url = Routes.getRoute(RouteIds.AllImages)
        Log.d(TAG, "requestData: start: url: $url, nethod: $method")
        Requestor.fetchCallback(method, url, JSONObject()) { result ->
            Log.d(TAG, "requestData: success: $result")
            if (result is Exception) {
                val message = result.message
                val stacktrace = result.getStackTrace().joinToString("\n")
                Log.d(TAG, "requestData: error: $message / Stacktrace: $stacktrace")
                Toast.makeText(this.context, "Error: $message", Toast.LENGTH_LONG).show()
            }
            else if (result is JSONObject && result.has("images") && result["images"] is JSONArray) {
                val images = result["images"] as JSONArray
                this.setImagesList(images)
            }
        }
    }

    fun reloadData() {
        this.requestData()
    }

    fun deleteAll() {
        val method = Request.Method.DELETE
        val url = Routes.getRoute(RouteIds.AllImages)
        Log.d(TAG, "deleteAll: start: url: $url, nethod: $method")
        Requestor.fetchCallback(method, url, JSONObject()) { result ->
            Log.d(TAG, "deleteAll: success: $result")
            if (result is Exception) {
                val message = result.message
                val stacktrace = result.getStackTrace().joinToString("\n")
                Log.d(TAG, "deleteAll: error: $message / Stacktrace: $stacktrace")
                Toast.makeText(this.context, "Error: $message", Toast.LENGTH_LONG).show()
            }
            else {
                // Alternative: pass empty images list in expected json format: `JSONObject(mapOf("images" to ArrayList<Int>()))`
                this.setImagesList(null)
            }
        }
    }

    fun deleteImage(id: String) {
        val method = Request.Method.DELETE
        val data = mapOf("id" to id)
        val url = Routes.getRoute(RouteIds.Image, data)
        Log.d(TAG, "deleteImage: start: url: $url, nethod: $method")
        Requestor.fetchCallback(method, url, JSONObject()) { result ->
            Log.d(TAG, "deleteImage: success: $result")
            if (result is Exception) {
                val message = result.message
                val stacktrace = result.getStackTrace().joinToString("\n")
                Log.d(TAG, "deleteImage: error: $message / Stacktrace: $stacktrace")
                Toast.makeText(this.context, "Error: $message", Toast.LENGTH_LONG).show()
            }
            else {
                // Alternative: pass empty images list in expected json format: `JSONObject(mapOf("images" to ArrayList<Int>()))`
                // TODO: filter ITEMS
                // this.setImagesList(null)
                val removedItem = this.ITEM_MAP.remove(id)
                if (removedItem !== null) {
                    this.ITEMS.remove(removedItem)
                }
                // val removedIndex = this.ITEMS.indexOf(removedItem)
                // this.ITEMS.drop(removedIndex)
                Log.d(TAG, "deleteImage: deleted image $id, ${this.ITEMS}, ${this.ITEM_MAP}")
                if (!(this::updateCallback.isInitialized)) {
                    throw Exception("`updateCallback` must be initialized!")
                }
                this.updateCallback()
            }
        }
    }

    private fun requestDataAsync() { // NOTE: Example!
        val method = Request.Method.GET
        val url = Routes.getRoute(RouteIds.AllImages)
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

    // private fun createImageItem(position: Int): ImageItem {
    //     return ImageItem(position.toString(), "Item " + position, makeDetails(position))
    // }
    // private fun makeDetails(position: Int): String {
    //     val builder = StringBuilder()
    //     builder.append("Details about Item: ").append(position)
    //     for (i in 0..position - 1) {
    //         builder.append("\nMore details information here.")
    //     }
    //     return builder.toString()
    // }

}
