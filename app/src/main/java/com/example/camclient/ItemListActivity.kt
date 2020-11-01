package com.example.camclient

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.widget.NestedScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.camclient.core.CoreContent

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ItemDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ItemListActivity : AppCompatActivity() {

    private val TAG: String = "DEBUG:ItemListActivity"

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet device.
     */
    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title

        findViewById<FloatingActionButton>(R.id.delete_button).setOnClickListener { view ->
            Snackbar.make(view, "Deleting all images", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            CoreContent.deleteAll()
        }

        findViewById<FloatingActionButton>(R.id.reload_button).setOnClickListener { view ->
            Snackbar.make(view, "Reloading images", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            CoreContent.reloadData()
        }

        // if (findViewById<NestedScrollView>(R.id.item_detail_container) != null) {
        if (findViewById<NestedScrollView>(R.id.item_details) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        // setupRecyclerView()
        CoreContent.start(this) { -> this.setupRecyclerView() }
    }

    private fun setupRecyclerView() {
        val items = CoreContent.ITEMS
        Log.d(TAG, "setupRecyclerView: $items")
        val recyclerView: RecyclerView = findViewById(R.id.item_list)
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, CoreContent.ITEMS, twoPane)
    }

    class SimpleItemRecyclerViewAdapter(private val parentActivity: ItemListActivity,
                                        private val values: List<CoreContent.ImageItem>,
                                        private val twoPane: Boolean) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as CoreContent.ImageItem
                if (twoPane) {
                    val fragment = ItemDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(ItemDetailFragment.ARG_ITEM_ID, item.id)
                        }
                    }
                    parentActivity.supportFragmentManager
                            .beginTransaction()
                            // .replace(R.id.item_detail_container, fragment)
                            .replace(R.id.item_details, fragment)
                            .commit()
                } else {
                    val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                        putExtra(ItemDetailFragment.ARG_ITEM_ID, item.id)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.ipView.text = item.ip
            holder.contentView.text = item.timestamp

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val ipView: TextView = view.findViewById(R.id.ip_text)
            val contentView: TextView = view.findViewById(R.id.content)
        }
    }

}
