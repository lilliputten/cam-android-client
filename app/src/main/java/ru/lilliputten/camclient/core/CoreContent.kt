/** @module CoreContent
 *  @since 2020.10.30, 03:27
 *  @changed 2020.11.02, 03:06
 *
 *  TODO:
 *  - Cache images list
 */

package ru.lilliputten.camclient.core

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import ru.lilliputten.camclient.config.RouteIds
import ru.lilliputten.camclient.config.Routes
import ru.lilliputten.camclient.helpers.Requestor
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
    val itemsList: MutableList<ImageItem> = ArrayList()

    /**
     * A map of image items, by ID.
     */
    val itemsDataMap: MutableMap<String, ImageItem> = HashMap()

    private val bitmapsCacheMap: MutableMap<String, Bitmap> = HashMap()

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
            this.itemsList.clear()
            this.itemsDataMap.clear()
            this.bitmapsCacheMap.clear()
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
            Log.d(TAG, "setImagesList: calling updateCallback with $this.itemsList")
            if (!(this::updateCallback.isInitialized)) {
                throw Exception("`updateCallback` must be initialized!")
            }
            this.updateCallback()
        }
        catch (ex: Exception) {
            val message = ex.message
            val stacktrace = ex.stackTrace.joinToString("\n")
            Log.d(TAG, "setImagesList: error: $message / Stacktrace: $stacktrace")
            Toast.makeText(this.context, "setImagesList: Error: $message", Toast.LENGTH_LONG).show()
        }
    }

    private fun requestData() {
        val method = Request.Method.GET
        val url = Routes.getRoute(RouteIds.AllImages)
        Log.d(TAG, "requestData: start: url: $url, method: $method")
        Requestor.fetchJsonCallback(method, url, JSONObject()) { result ->
            Log.d(TAG, "requestData: result: $result")
            if (result is Exception) {
                val message = result.message
                val stacktrace = result.stackTrace.joinToString("\n")
                Log.d(TAG, "requestData: error: $message / Stacktrace: $stacktrace")
                Toast.makeText(this.context, "Error: $message", Toast.LENGTH_LONG).show()
            }
            else if (result is JSONObject && result.has("images") && result["images"] is JSONArray) {
                val images = result["images"] as JSONArray
                Log.d(TAG, "requestData: success: $images")
                this.setImagesList(images)
            }
        }
    }

    fun reloadData() {
        this.requestData()
    }

    fun getImageData(id: String, callback: (Any) -> Unit) {
        Log.d(TAG, "getImageData: start: id: $id")
        if (this.bitmapsCacheMap.containsKey(id)) {
            val bitmap = this.bitmapsCacheMap[id]
            Log.d(TAG, "getImageData: found in cache: $bitmap")
            callback(bitmap as Any)
            return
        }
        val data = mapOf("id" to id)
        val url = Routes.getRoute(RouteIds.ShowImage, data)
        Log.d(TAG, "getImageData: request start: url: $url, data: $data")
        // TODO: Store & fetch image data from cache?
        Requestor.fetchImageCallback(url) { result ->
            Log.d(TAG, "getImageData: request result: $result")
            if (result is Exception) {
                val message = result.message
                val stacktrace = result.stackTrace.joinToString("\n")
                Log.d(TAG, "getImageData: request error: $message / Stacktrace: $stacktrace")
                // Toast.makeText(this.context, "Error: $message", Toast.LENGTH_LONG).show()
                callback(result)
            }
            // else if (result is JSONObject && result.has("images") && result["images"] is JSONArray) {
            else if (result is Bitmap) {
                Log.d(TAG, "getImageData: request success: $result")
                this.bitmapsCacheMap[id] = result
                callback(result)
            }
        }
    }

    fun deleteAll() {
        val method = Request.Method.DELETE
        val url = Routes.getRoute(RouteIds.AllImages)
        Log.d(TAG, "deleteAll: start: url: $url, method: $method")
        Requestor.fetchJsonCallback(method, url, JSONObject()) { result ->
            Log.d(TAG, "deleteAll: success: $result")
            if (result is Exception) {
                val message = result.message
                val stacktrace = result.stackTrace.joinToString("\n")
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
        Log.d(TAG, "deleteImage: start: url: $url, method: $method")
        Requestor.fetchJsonCallback(method, url, JSONObject()) { result ->
            Log.d(TAG, "deleteImage: success: $result")
            if (result is Exception) {
                val message = result.message
                val stacktrace = result.stackTrace.joinToString("\n")
                Log.d(TAG, "deleteImage: error: $message / Stacktrace: $stacktrace")
                Toast.makeText(this.context, "Error: $message", Toast.LENGTH_LONG).show()
            }
            else {
                // Alternative: pass empty images list in expected json format: `JSONObject(mapOf("images" to ArrayList<Int>()))`
                // TODO: filter itemsList
                // this.setImagesList(null)
                val removedItem = this.itemsDataMap.remove(id)
                if (removedItem !== null) {
                    this.itemsList.remove(removedItem)
                }
                if (this.bitmapsCacheMap.containsKey(id)) {
                    this.bitmapsCacheMap.remove(id)
                }
                // val removedIndex = this.itemsList.indexOf(removedItem)
                // this.itemsList.drop(removedIndex)
                Log.d(TAG, "deleteImage: deleted image $id")
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
                val stacktrace = ex.stackTrace.joinToString("\n")
                Log.d(TAG, "requestData: error: $message / Stacktrace: $stacktrace")
            }
        }
        Log.d(TAG, "requestData: after coroutine")
    }

    private fun addItem(item: ImageItem) {
        this.itemsList.add(item)
        this.itemsDataMap[item.id] = item
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
