/** @module ItemDetailFragment
 *  @since 2020.10.30, 03:27
 *  @changed 2020.11.08, 00:33
 */

package ru.lilliputten.camclient

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.android.volley.toolbox.ImageLoader
import ru.lilliputten.camclient.config.RouteIds
import ru.lilliputten.camclient.config.Routes
import ru.lilliputten.camclient.core.CoreContent

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListActivity]
 * in two-pane mode (on tablets) or a [ItemDetailActivity]
 * on handsets.
 */
class ItemDetailFragment : Fragment() {

    private val TAG: String = "DEBUG:ItemDetailFragment"

    /**
     * The content this fragment is presenting.
     */
    private var item: CoreContent.ImageItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                item = CoreContent.itemsDataMap[it.getString(ARG_ITEM_ID)]
                // activity?.findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)?.title = "${item?.timestamp}\n${item?.ip}"
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        try {
            if (container != null) {
                // val rootView = inflater.inflate(R.layout.item_detail, container, false)
                val viewId = R.layout.item_details
                Log.d(TAG, "onCreateView: try to find view: $viewId")
                val rootView = inflater.inflate(viewId, container, false)
                val topLevelView = container.parent.parent as CoordinatorLayout
                Log.d(TAG, "onCreateView: found view: $rootView")
                val progressSpinner = topLevelView.findViewById<ProgressBar>(R.id.details_progress_spinner)
                // Log.d(TAG, "onCreate: progressSpinner: $progressSpinner")
                // Show content
                item?.let {
                    val id = it.id

                    // rootView.findViewById<TextView>(R.id.item_detail).text = "Image for item ${it.id}"
                    // val showTimestamp = rootView.findViewById<TextView>(R.id.show_timestamp)
                    val showTimestamp = container.findViewById<TextView>(R.id.show_timestamp)
                    Log.d(TAG, "onCreateView: found timestamp field (${R.id.show_timestamp}): $showTimestamp")
                    if (showTimestamp !== null) {
                        showTimestamp.text = it.timestamp
                    }
                    val showIp = container.findViewById<TextView>(R.id.show_ip)
                    Log.d(TAG, "onCreateView: found ip field (${R.id.show_ip}): $showIp")
                    if (showIp !== null) {
                        showIp.text = it.ip
                    }
                    val showImage = container.findViewById<ImageView>(R.id.show_image)

                    progressSpinner.visibility = View.VISIBLE

                    CoreContent.getImageData(id) { result ->
                        progressSpinner.visibility = View.GONE
                        Log.d(TAG, "onCreateView: getImageData: result: $result, showImage: $showImage")
                        if (result is Exception) {
                            val message = result.message
                            val stacktrace = result.stackTrace.joinToString("\n")
                            Log.d(TAG, "onCreateView: getImageData: error: $message / Stacktrace: $stacktrace")
                            Toast.makeText(this.context, "Error: $message", Toast.LENGTH_LONG).show()
                        }
                        // else if (result is JSONObject && result.has("images") && result["images"] is JSONArray) {
                        else if (result is Bitmap && showImage !== null) {
                            Log.d(TAG, "onCreateView: getImageData: success: $result")
                            showImage.setImageBitmap(result)
                        }
                    }
                }
                return rootView
            }
        }
        catch (ex: Exception) {
            val message = if (ex.message != null) ex.message else ex.toString()
            val stacktrace = ex.stackTrace.joinToString("\n")
            Log.d(TAG, "onCreateView: exception: $message / $stacktrace")
            Toast.makeText(this.context, "Exception: $message", Toast.LENGTH_LONG).show()
        }
        return null
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }

}
