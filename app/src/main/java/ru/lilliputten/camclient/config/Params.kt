/** @module config/Params
 *  @since 2020.10.30, 03:27
 *  @changed 2020.10.30, 05:02
 */

package ru.lilliputten.camclient.config

import ru.lilliputten.camclient.config.CoreUtils

// import android.util.Log
// import java.util.*

val coreIsEmulator = CoreUtils.isEmulator()

object Params {

    private const val TAG: String = "DEBUG:Params"

    val isEmulator = coreIsEmulator // CoreUtils.isEmulator()

    val serverAddr = "https://cam.lilliputten.ru"
    val devServerAddr = "http://10.0.2.2:5000"

    val authLogin = "guest"
    val authPass = "123"

}
