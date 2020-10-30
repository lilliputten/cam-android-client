/** @module helpers/Requestor
 *  @since 2020.10.30, 03:27
 *  @changed 2020.10.31, 01:37
 */

package com.example.camclient.helpers

import android.content.Context
import android.service.autofill.Validators.not
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.Volley
import java.util.HashMap
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

import com.example.camclient.config.CoreUtils
import com.example.camclient.config.Params
import com.example.camclient.config.RouteIds
import com.example.camclient.config.Routes

class RequestorException(message:String): Exception(message)

object Requestor {

    private const val TAG: String = "DEBUG:Requestor"

    private lateinit var context: Context
    private lateinit var queue: RequestQueue

    // init {
    //     // Toast.makeText(this, "Init!", Toast.LENGTH_LONG).show()
    // }

    fun start(context: Context) {
        if (context === null) {
            throw RequestorException("Requestor: Context must be passed in start method!")
        }
        this.context = context
        this.queue = Volley.newRequestQueue(this.context)
        Log.d(TAG, "start: context: ${this.context} queue: ${this.queue}")
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
        throw ex
        // deferred.completeExceptionally(ex)
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
                Toast.makeText(context, "Request success!", Toast.LENGTH_LONG).show()
                Log.d(TAG, "requestData: response: %s".format(response.toString()))
                deferred.complete(response)
            },
            Response.ErrorListener { error -> // Handle error
                val errorStr = error.toString()
                val exStr = "Request error: $errorStr"
                Toast.makeText(context, exStr, Toast.LENGTH_LONG).show()
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

    fun fetchCallback(method: Int, url: String?, data: JSONObject?, callback: (JSONObject) -> Unit) {
        val url = Routes.getRoute(RouteIds.Recent)
        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, JSONObject(),
            Response.Listener { response ->
                Toast.makeText(this.context, "Request success!", Toast.LENGTH_LONG).show()
                Log.d(TAG, "requestData: response: %s".format(response.toString()))
                callback(response)
            },
            Response.ErrorListener { error -> // Handle error
                val errorStr = error.toString()
                val exStr = "Request error: $errorStr"
                Toast.makeText(context, exStr, Toast.LENGTH_LONG).show()
                Log.d(TAG, "requestData: error: $errorStr")
                val ex = RequestorException(exStr)
                throw ex
                // callback(null, ex)
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
    }

}
