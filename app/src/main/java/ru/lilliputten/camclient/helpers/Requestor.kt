/** @module helpers/Requestor
 *  @since 2020.10.30, 03:27
 *  @changed 2020.11.08, 00:33
 */

package ru.lilliputten.camclient.helpers

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.util.HashMap
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

import ru.lilliputten.camclient.config.Params

class RequestorException(message:String): Exception(message)

object Requestor {

    private const val TAG: String = "DEBUG:Requestor"

    // private var inited: Boolean = false

    private lateinit var context: Context
    private lateinit var queue: RequestQueue

    // init {
    //     Log.d(TAG, "init")
    // }

    fun start(context: Context) {
        this.context = context
        this.queue = Volley.newRequestQueue(this.context)
        Log.d(TAG, "start: ${this.context} queue: ${this.queue}")
        // if (!this.inited) {
        //     this.inited = true
        // }
        // else {
        //     Log.d(TAG, "start: already inited: context: ${this.context} queue: ${this.queue}")
        // }
    }

    /**
     * Unused?
     */
    fun fetchStringCallback(method: Int, url: String?, data: Map<String, String>?, callback: (Any) -> Unit) {
        if (!(this::queue.isInitialized)) {
            throw RequestorException("Queue not initialized. Method `start(context)` must be invoked first.")
        }
        if (url == null) {
            throw RequestorException("Url is not initialized.")
        }
        Log.d(TAG, "fetchStringCallback: method: $method, url: $url, data: $data")
        val jsonObjectRequest = object : StringRequest(method, url,
            Response.Listener { response: String ->
                Log.d(TAG, "fetchStringCallback: response: $response")
                callback(response)
            },
            Response.ErrorListener { error -> // Handle error
                val message = if (error.message !== null) error.message else error.toString()
                // Message samples:
                // com.android.volley.TimeoutError -- on unaccessible host
                val stacktrace = error.stackTrace.joinToString("\n")
                val exStr = "Request error: $message"
                // Toast.makeText(context, exStr, Toast.LENGTH_LONG).show()
                Log.d(TAG, "fetchStringCallback: error: $message / $stacktrace")
                val ex = RequestorException(exStr)
                // throw ex
                callback(ex)
            }
        ) {
            // Add authorization header (like 'Authorization: Basic Z3Vlc3Q6MTIz')
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>() // It works!
                val cred:String = String.format("%s:%s", Params.authLogin, Params.authPass)
                val auth = "Basic " + Base64.encodeToString(cred.toByteArray(), Base64.NO_WRAP)
                Log.d(TAG, "fetchStringCallback: auth: $auth")
                headers["Authorization"] = auth
                return headers
            }
        }
        this.queue.add(jsonObjectRequest)
    }

    fun fetchCustomCallback(method: Int, url: String?, data: Map<String, String>?, callback: (Any) -> Unit) { // DEMO!
        if (!(this::queue.isInitialized)) {
            throw RequestorException("Queue not initialized. Method `start(context)` must be invoked first.")
        }
        if (url == null) {
            throw RequestorException("Url is not initialized.")
        }
        Log.d(TAG, "fetchCustomCallback: method: $method, url: $url, data: $data")
        // val jsonObjectRequest = object : JsonObjectRequest(method, url, data,
        val jsonObjectRequest = object : StringRequest(method, url,
            Response.Listener { response: String ->
                Log.d(TAG, "fetchCustomCallback: response: $response")
                callback(response)
            },
            Response.ErrorListener { error -> // Handle error
                val message = if (error.message !== null) error.message else error.toString()
                // Message samples:
                // com.android.volley.TimeoutError -- on unaccessible host
                val stacktrace = error.stackTrace.joinToString("\n")
                val exStr = "Request error: $message"
                Log.d(TAG, "fetchCustomCallback: error: $message / $stacktrace")
                val ex = RequestorException(exStr)
                callback(ex)
            }
        ) {
            // Add authorization header (like 'Authorization: Basic Z3Vlc3Q6MTIz')
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>() // It works!
                val cred:String = String.format("%s:%s", Params.authLogin, Params.authPass)
                val auth = "Basic " + Base64.encodeToString(cred.toByteArray(), Base64.NO_WRAP)
                Log.d(TAG, "fetchCustomCallback: auth: $auth")
                headers["Authorization"] = auth
                return headers
            }
            // // TODO: Pass data parameters
            // @Override
            // protected Map<String, String> getParams()
            // {
            //     params: Map<String, String> = new HashMap<String, String>()
            //     params.put("name", "Alif")
            //     params.put("domain", "http://itsalif.info")
            //     return params
            // }
        }
        // MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
        this.queue.add(jsonObjectRequest)
        // catch (ex: Exception) {
        //     val message = ex.message
        //     val stacktrace = ex.stackTrace.joinToString("\n")
        //     Toast.makeText(context, "requestData: exception: $message", Toast.LENGTH_LONG).show()
        //     Log.d(TAG, "requestData: exception: $message / $stacktrace")
        //     val ex = RequestorException("requestData exception: $message")
        //     callback(ex)
        // }
    }

    fun fetchImageCallback(url: String?, callback: (Any) -> Unit) {
        if (!(this::queue.isInitialized)) {
            throw RequestorException("Queue not initialized. Method `start(context)` must be invoked first.")
        }
        if (url == null) {
            throw RequestorException("Url is not initialized.")
        }
        Log.d(TAG, "fetchImageCallback: url: $url")
        // val jsonObjectRequest = object : JsonObjectRequest(method, url, data,
        val jsonObjectRequest = object : ImageRequest(url,
            Response.Listener { bitmap: Bitmap ->
                Log.d(TAG, "fetchImageCallback: bitmap: $bitmap")
                callback(bitmap)
            }, 0, 0, ImageView.ScaleType.CENTER_INSIDE, Bitmap.Config.RGB_565,
            Response.ErrorListener { error -> // Handle error
                val message = if (error.message !== null) error.message else error.toString()
                // Message samples:
                // com.android.volley.TimeoutError -- on unaccessible host
                val stacktrace = error.stackTrace.joinToString("\n")
                val exStr = "Request error: $message"
                // Toast.makeText(context, exStr, Toast.LENGTH_LONG).show()
                Log.d(TAG, "fetchImageCallback: error: $message / $stacktrace")
                val ex = RequestorException(exStr)
                callback(ex)
            }
        ) {
            // Add authorization header (like 'Authorization: Basic Z3Vlc3Q6MTIz')
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>() // It works!
                val cred:String = String.format("%s:%s", Params.authLogin, Params.authPass)
                val auth = "Basic " + Base64.encodeToString(cred.toByteArray(), Base64.NO_WRAP)
                Log.d(TAG, "fetchImageCallback: auth: $auth")
                headers["Authorization"] = auth
                return headers
            }
            // // TODO: Pass data parameters
            // @Override
            // protected Map<String, String> getParams()
            // {
            //     params: Map<String, String> = new HashMap<String, String>()
            //     params.put("name", "Alif")
            //     params.put("domain", "http://itsalif.info")
            //     return params
            // }
        }
        // MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
        this.queue.add(jsonObjectRequest)
    }

    fun fetchJsonCallback(method: Int, url: String?, data: JSONObject?, callback: (Any) -> Unit) {
        if (!(this::queue.isInitialized)) {
            throw RequestorException("Queue not initialized. Method `start(context)` must be invoked first.")
        }
        if (url == null) {
            throw RequestorException("Url is not initialized.")
        }
        Log.d(TAG, "fetchJsonCallback: method: $method, url: $url, data: $data")
        val jsonObjectRequest = object : JsonObjectRequest(method, url, data,
            Response.Listener { response ->
                Log.d(TAG, "fetchJsonCallback: response: %s".format(response.toString()))
                try {
                    if (response.has("error")) { // Error?
                        val error = response["error"]
                        Log.d(TAG, "fetchJsonCallback: response: error: $error")
                        val ex = RequestorException("Server error: $error")
                        callback(ex)
                    }
                    else { // Success
                        // Toast.makeText(this.context, "Request success!", Toast.LENGTH_LONG).show()
                        Log.d(TAG, "fetchJsonCallback: response: success: %s".format(response.toString()))
                        callback(response)
                    }
                }
                catch (ex: Exception) {
                    val message = ex.message
                    val stacktrace = ex.stackTrace.joinToString("\n")
                    Log.d(TAG, "fetchJsonCallback: response: exception: $message / $stacktrace")
                    callback(ex)
                }
            },
            Response.ErrorListener { error -> // Handle error
                val message = if (error.message !== null) error.message else error.toString()
                // Message samples:
                // com.android.volley.TimeoutError -- on unaccessible host
                val stacktrace = error.stackTrace.joinToString("\n")
                val exStr = "Request error: $message"
                // Toast.makeText(context, exStr, Toast.LENGTH_LONG).show()
                Log.d(TAG, "fetchJsonCallback: error: $message / $stacktrace")
                val ex = RequestorException(exStr)
                // throw ex
                callback(ex)
            }
        ) {
            // Add authorization header (like 'Authorization: Basic Z3Vlc3Q6MTIz')
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>() // It works!
                val cred:String = String.format("%s:%s", Params.authLogin, Params.authPass)
                val auth = "Basic " + Base64.encodeToString(cred.toByteArray(), Base64.NO_WRAP)
                Log.d(TAG, "fetchJsonCallback: auth: $auth")
                headers["Authorization"] = auth
                return headers
            }
        }
        this.queue.add(jsonObjectRequest)
        // catch (ex: Exception) {
        //     val message = ex.message
        //     val stacktrace = ex.stackTrace.joinToString("\n")
        //     Toast.makeText(context, "requestData: exception: $message", Toast.LENGTH_LONG).show()
        //     Log.d(TAG, "requestData: exception: $message / $stacktrace")
        //     val ex = RequestorException("requestData exception: $message")
        //     callback(ex)
        // }
    }

    fun runTestAsyncResult() {
        // NOTE: Test for processing async functions
        if (!(this::queue.isInitialized)) { // NOTE: Test for initialized property
            Log.d(TAG, "Before testAsyncResult ${this.queue}")
        }
        runBlocking {
            try {
                val result = Requestor.testAsyncResult().await()
                Log.d(TAG, "After testAsyncResult success: $result")
            }
            catch (ex: Exception) {
                val message = ex.message
                val stacktrace = ex.stackTrace.joinToString("\n")
                Log.d(TAG, "After testAsyncResult error: $message / $stacktrace")
            }
        }
    }

    private fun testAsyncResult(): CompletableDeferred<Boolean> {
        val deferred = CompletableDeferred<Boolean>()
        // val result = JSONObject()
        // result.put("ok", "OK")
        // deferred.complete(true)
        val ex = RequestorException("Test error")
        // throw ex
        deferred.completeExceptionally(ex)
        return deferred
    }

    fun fetchDeferred(method: Int, url: String?, data: JSONObject?): CompletableDeferred<JSONObject> { // DEMO!
        val deferred = CompletableDeferred<JSONObject>()
        if (!(this::queue.isInitialized)) {
            throw RequestorException("Queue not initialized. Method `start(context)` must be invoked first.")
        }
        if (url == null) {
            throw RequestorException("Url is not initialized.")
        }
        Log.d(TAG, "requestData: method: $method, url: $url, data: $data")
        val jsonObjectRequest = object : JsonObjectRequest(method, url, data,
            Response.Listener { response -> // Successfull response
                // Toast.makeText(context, "Request success!", Toast.LENGTH_LONG).show()
                Log.d(TAG, "fetchDeferred: response: %s".format(response.toString()))
                deferred.complete(response)
            },
            Response.ErrorListener { error -> // Handle error
                val errorStr = error.toString()
                val exStr = "Request error: $errorStr"
                // Toast.makeText(context, exStr, Toast.LENGTH_LONG).show()
                Log.d(TAG, "requestData: error: $errorStr")
                val ex = RequestorException(exStr)
                deferred.completeExceptionally(ex)
            }
        ) {
            // Add authorization header (like 'Authorization: Basic Z3Vlc3Q6MTIz')
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>() // It works!
                val cred:String = String.format("%s:%s", Params.authLogin, Params.authPass)
                val auth = "Basic " + Base64.encodeToString(cred.toByteArray(), Base64.NO_WRAP)
                Log.d(TAG, "requestData: auth: $auth")
                headers["Authorization"] = auth
                return headers
            }
        }
        this.queue.add(jsonObjectRequest)
        return deferred
    }

}
