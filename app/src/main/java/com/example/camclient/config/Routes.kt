/** @module config/Routes
 *  @since 2020.10.30, 03:27
 *  @changed 2020.10.30, 05:02
 */

package com.example.camclient.config

import android.util.Log
import java.util.*

import com.example.camclient.config.Params
import com.example.camclient.config.RouteIds

val isEmulator = Params.isEmulator
val server = if (isEmulator) Params.devServerAddr else Params.serverAddr
val routesMap = mapOf(
    RouteIds.Recent to "$server/api/images/recent",
    RouteIds.AllImages to "$server/api/images",
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

    fun getRoute(id: RouteIds): String? {
        // val routesMap = this.getRoutesMap()
        Log.d(TAG, "Routes: getRoute: $id")
        return routesMap[id]
    }

}
