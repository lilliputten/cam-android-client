/** @module config/CoreUtils
 *  @since 2020.10.30, 03:27
 *  @changed 2020.10.30, 05:02
 */

package com.example.masterdetail.config

import android.os.Build
import android.util.Log

var routesList: HashMap<String, String> = hashMapOf(
    "recent" to "http://10.0.2.2:5000/api/images/recent",
)

object CoreUtils {

    private const val TAG: String = "DEBUG:CoreUtils"

    init {
        val buildInfo = this.getBuildInfo()
        Log.d(TAG, "init: build info:\n$buildInfo")
    }

    fun getBuildInfo(): String {
        // Result samples:
        //
        // Emulator (`emulator-5554          device product:sdk_gphone_x86_arm model:sdk_gphone_x86_arm device:generic_x86_arm transport_id:1`)
        //
        // FINGERPRINT:google/sdk_gphone_x86_arm/generic_x86_arm:11/RSR1.201013.001/6903271:userdebug/dev-keys
        // MODEL:sdk_gphone_x86_arm
        // MANUFACTURER:Google
        // BRAND:google
        // DEVICE:generic_x86_arm
        // BOARD:goldfish_x86
        // HOST:abfarm-us-west1-c-0007
        // PRODUCT:sdk_gphone_x86_arm
        // HARDWARE:ranchu
        //
        // HUAWEI MediaPad M5 Lite (`52E9X19822005296       device product:JDN2-W09 model:JDN2_W09 device:HWJDN2 transport_id:3`)
        return "FINGERPRINT:${Build.FINGERPRINT}\n" +
               "MODEL:${Build.MODEL}\n" +
               "MANUFACTURER:${Build.MANUFACTURER}\n" +
               "BRAND:${Build.BRAND}\n" +
               "DEVICE:${Build.DEVICE}\n" +
               "BOARD:${Build.BOARD}\n" +
               "HOST:${Build.HOST}\n" +
               "PRODUCT:${Build.PRODUCT}\n" +
               "HARDWARE:${Build.HARDWARE}\n"
    }

    fun isEmulator(): Boolean {
        // See `getBuildInfo` result samples above
        return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
            || Build.FINGERPRINT.startsWith("generic")
            || Build.FINGERPRINT.startsWith("unknown")
            || Build.HARDWARE.contains("goldfish")
            || Build.HARDWARE.contains("ranchu")
            || Build.MODEL.contains("google_sdk")
            || Build.MODEL.contains("Emulator")
            || Build.MODEL.contains("Android SDK built for x86")
            || Build.MANUFACTURER.contains("Genymotion")
            || Build.PRODUCT.contains("sdk_google")
            || Build.PRODUCT.contains("google_sdk")
            || Build.PRODUCT.contains("sdk")
            || Build.PRODUCT.contains("sdk_x86")
            || Build.PRODUCT.contains("vbox86p")
            || Build.PRODUCT.contains("emulator")
            || Build.PRODUCT.contains("simulator")
            || Build.BOARD.startsWith("goldfish")
    }

}
