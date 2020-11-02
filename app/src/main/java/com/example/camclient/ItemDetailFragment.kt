/** @module ItemDetailFragment
 *  @since 2020.10.30, 03:27
 *  @changed 2020.11.02, 03:05
 */

package com.example.camclient

// import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.CollapsingToolbarLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
// import android.widget.Toast
import com.example.camclient.core.CoreContent
// import com.google.android.material.snackbar.Snackbar

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
                item = CoreContent.ITEM_MAP[it.getString(ARG_ITEM_ID)]
                // activity?.findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)?.title = "${item?.timestamp}\n${item?.ip}"
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        try {
            // val rootView = inflater.inflate(R.layout.item_detail, container, false)
            val viewId = R.layout.item_details
            // val viewId = R.layout.show_timestamp_row
            Log.d(TAG, "onCreateView: try to find view: $viewId")
            // return null
            val rootView = inflater.inflate(viewId, container, false)
            Log.d(TAG, "onCreateView: found view: $rootView")
            // Show content
            item?.let {
                // rootView.findViewById<TextView>(R.id.item_detail).text = "Image for item ${it.id}"
                // val showTimestamp = rootView.findViewById<TextView>(R.id.show_timestamp)
                val showTimestamp = container?.findViewById<TextView>(R.id.show_timestamp)
                Log.d(TAG, "onCreateView: found timestamp field (${R.id.show_timestamp}): $showTimestamp")
                if (showTimestamp !== null) {
                    showTimestamp.text = it.timestamp
                }
                val showIp = container?.findViewById<TextView>(R.id.show_ip)
                Log.d(TAG, "onCreateView: found ip field (${R.id.show_ip}): $showIp")
                if (showIp !== null) {
                    showIp.text = it.ip
                }
                // TODO 2020.11.01, 05:19 -- Place image
            }
            return rootView
        }
        catch (ex: Exception) {
            val message = ex.message
            val stacktrace = ex.getStackTrace().joinToString("\n")
            Log.d(TAG, "onCreateView: exception: $message / $stacktrace")
            return null
        }
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }
}
