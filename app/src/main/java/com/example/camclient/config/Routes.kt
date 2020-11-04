/** @module config/Routes
 *  @since 2020.10.30, 03:27
 *  @changed 2020.11.02, 03:06
 */

package com.example.camclient.config

import android.util.Log
import java.util.*

import com.example.camclient.config.Params
import com.example.camclient.config.RouteIds

val isEmulator = Params.isEmulator
val server = if (false && isEmulator) Params.devServerAddr else Params.serverAddr
val routesMap = mapOf(
    RouteIds.Recent to "$server/api/images/recent",
    RouteIds.AllImages to "$server/api/images",
    RouteIds.Image to "$server/api/images/{id}",
    RouteIds.ShowImage to "$server/image/{id}-",
)

object Routes {

    private const val TAG: String = "DEBUG:Routes"

    // val Ids by lazy { RouteIds }

    init {
        Log.d(TAG, "Routes: init")
    }

    // private fun getRoutesMap():  Map<RouteIds, String> {
    //     val isEmulator = Params.isEmulator
    //     val server = if (isEmulator) Params.devServerAddr else Params.serverAddr
    //     val routesMap = mapOf(
    //         RouteIds.Recent to "$server/api/images/recent",
    //         RouteIds.Recent to "$server/api/images/recent",
    //     )
    //     val routesMapStr = routesMap.mapValues { it.value.toString() }
    //     Log.d(TAG, "Routes: getRoutesMap: isEmulator: $isEmulator / server: $server -> routesMap: $routesMapStr")
    //     return routesMap
    // }

    fun getRoute(id: RouteIds, data: Map<String, String>? = null): String? {
        // val routesMap = this.getRoutesMap()
        Log.d(TAG, "Routes: getRoute: $id")
        var url = routesMap[id]
        try {
            if (data !== null && url != null) {
                Log.d(TAG, "getRoute: replace: $url")
                url = url.replace(regex = Regex("""\{(\w+)\}"""), transform = { match ->
                    val key = match.groups[1]?.value
                    if (key !== null && data[key] !== null) data[key].toString() else match.value
                })
                Log.d(TAG, "getRoute: replaced: $url")
            }
        }
        catch (ex: Exception) {
            val message = ex.message
            val stacktrace = ex.getStackTrace().joinToString("\n")
            Log.d(TAG, "getRoute: error: $message / Stacktrace: $stacktrace")
        }
        return url
    }

}
