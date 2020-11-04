/** @module helpers/Requestor
 *  @since 2020.10.30, 03:27
 *  @changed 2020.10.31, 21:35
 */

package com.example.camclient.helpers

import android.content.Context
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.util.HashMap
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

import com.example.camclient.config.Params

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
        // val url = Routes.getRoute(RouteIds.Recent)
        if (!(this::queue.isInitialized)) {
            throw RequestorException("Queue not initialized. Method `start(context)` must be invoked first.")
        }
        if (url == null) {
            throw RequestorException("Url is not initialized.")
        }
        Log.d(TAG, "fetchStringCallback: method: $method, url: $url, data: $data")
        // val jsonObjectRequest = object : JsonObjectRequest(method, url, data,
        val jsonObjectRequest = object : StringRequest(method, url,
            Response.Listener { response: String ->
                Log.d(TAG, "fetchStringCallback: response: $response")
                callback(response)
                // TODO: Process response?
                // try {
                //     if (response.has("error")) { // Error?
                //         val error = response["error"]
                //         Log.d(TAG, "fetchStringCallback: response: error: $error")
                //         val ex = RequestorException("Server error: $error")
                //         callback(ex)
                //     }
                //     else { // Success
                //         // Toast.makeText(this.context, "Request success!", Toast.LENGTH_LONG).show()
                //         Log.d(TAG, "fetchStringCallback: response: success: %s".format(response.toString()))
                //         callback(response)
                //     }
                // }
                // catch (ex: Exception) {
                //     val message = ex.message
                //     val stacktrace = ex.getStackTrace().joinToString("\n")
                //     Log.d(TAG, "fetchStringCallback: response: exception: $message / $stacktrace")
                //     callback(ex)
                // }
            },
            Response.ErrorListener { error -> // Handle error
                val message = if (error.message !== null) error.message else error.toString()
                // Message samples:
                // com.android.volley.TimeoutError -- on unaccessible host
                val stacktrace = error.getStackTrace().joinToString("\n")
                // val errorStr = error.toString()
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
        //     val stacktrace = ex.getStackTrace().joinToString("\n")
        //     Toast.makeText(context, "requestData: exception: $message", Toast.LENGTH_LONG).show()
        //     Log.d(TAG, "requestData: exception: $message / $stacktrace")
        //     val ex = RequestorException("requestData exception: $message")
        //     callback(ex)
        // }
    }

    fun fetchImageCallback(method: Int, url: String?, data: Map<String, String>?, callback: (Any) -> Unit) {
        // val url = Routes.getRoute(RouteIds.Recent)
        if (!(this::queue.isInitialized)) {
            throw RequestorException("Queue not initialized. Method `start(context)` must be invoked first.")
        }
        if (url == null) {
            throw RequestorException("Url is not initialized.")
        }
        Log.d(TAG, "fetchImageCallback: method: $method, url: $url, data: $data")
        // val jsonObjectRequest = object : JsonObjectRequest(method, url, data,
        val jsonObjectRequest = object : StringRequest(method, url,
            Response.Listener { response: String ->
                Log.d(TAG, "fetchImageCallback: response: $response")
                callback(response)
                // TODO: Process response?
                // try {
                //     if (response.has("error")) { // Error?
                //         val error = response["error"]
                //         Log.d(TAG, "fetchImageCallback: response: error: $error")
                //         val ex = RequestorException("Server error: $error")
                //         callback(ex)
                //     }
                //     else { // Success
                //         // Toast.makeText(this.context, "Request success!", Toast.LENGTH_LONG).show()
                //         Log.d(TAG, "fetchImageCallback: response: success: %s".format(response.toString()))
                //         callback(response)
                //     }
                // }
                // catch (ex: Exception) {
                //     val message = ex.message
                //     val stacktrace = ex.getStackTrace().joinToString("\n")
                //     Log.d(TAG, "fetchImageCallback: response: exception: $message / $stacktrace")
                //     callback(ex)
                // }
            },
            Response.ErrorListener { error -> // Handle error
                val message = if (error.message !== null) error.message else error.toString()
                // Message samples:
                // com.android.volley.TimeoutError -- on unaccessible host
                val stacktrace = error.getStackTrace().joinToString("\n")
                // val errorStr = error.toString()
                val exStr = "Request error: $message"
                // Toast.makeText(context, exStr, Toast.LENGTH_LONG).show()
                Log.d(TAG, "fetchImageCallback: error: $message / $stacktrace")
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
        // catch (ex: Exception) {
        //     val message = ex.message
        //     val stacktrace = ex.getStackTrace().joinToString("\n")
        //     Toast.makeText(context, "requestData: exception: $message", Toast.LENGTH_LONG).show()
        //     Log.d(TAG, "requestData: exception: $message / $stacktrace")
        //     val ex = RequestorException("requestData exception: $message")
        //     callback(ex)
        // }
    }

    fun fetchJsonCallback(method: Int, url: String?, data: JSONObject?, callback: (Any) -> Unit) {
        // val url = Routes.getRoute(RouteIds.Recent)
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
                    val stacktrace = ex.getStackTrace().joinToString("\n")
                    Log.d(TAG, "fetchJsonCallback: response: exception: $message / $stacktrace")
                    callback(ex)
                }
            },
            Response.ErrorListener { error -> // Handle error
                val message = if (error.message !== null) error.message else error.toString()
                // Message samples:
                // com.android.volley.TimeoutError -- on unaccessible host
                val stacktrace = error.getStackTrace().joinToString("\n")
                // val errorStr = error.toString()
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
        //     val stacktrace = ex.getStackTrace().joinToString("\n")
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
                val stacktrace = ex.getStackTrace().joinToString("\n")
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

    fun fetchDeferred(method: Int, url: String?, data: JSONObject?): CompletableDeferred<JSONObject> {
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
