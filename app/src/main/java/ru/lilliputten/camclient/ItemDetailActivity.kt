/** @module ItemDetailActivity
 *  @since 2020.10.30, 03:27
 *  @changed 2020.11.08, 00:33
 */

package ru.lilliputten.camclient

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
// import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import ru.lilliputten.camclient.core.CoreContent

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [ItemListActivity].
 */
class ItemDetailActivity : AppCompatActivity() {

    private val TAG: String = "DEBUG:ItemDetailActivity"

    private var itemId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        setSupportActionBar(findViewById(R.id.detail_toolbar))

        val progressSpinner = findViewById<ProgressBar>(R.id.details_progress_spinner)
        Log.d(TAG, "onCreate: progressSpinner: $progressSpinner")

        findViewById<FloatingActionButton>(R.id.delete_button).setOnClickListener {
          if (this.itemId === null) {
                throw Exception("itemId is undefined!")
            }
            else {
                progressSpinner.visibility = View.VISIBLE
                Log.d(TAG, "delete_button action: ${this.itemId}")
                Toast.makeText(this, "Deleting mage", Toast.LENGTH_LONG).show()
                // Snackbar.make(view, "Deleting item", Snackbar.LENGTH_LONG)
                //         .setAction("Action", null).show()
                CoreContent.deleteImage(this.itemId as String) {
                    progressSpinner.visibility = View.GONE
                    Toast.makeText(this, "Image was deleted", Toast.LENGTH_LONG).show()
                }
                navigateUpTo(Intent(this, ItemListActivity::class.java))
            }
        }

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don"t need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            val itemId = intent.getStringExtra(ItemDetailFragment.ARG_ITEM_ID)
            this.itemId = itemId
            val fragment = ItemDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ItemDetailFragment.ARG_ITEM_ID, itemId)
                }
            }
            supportFragmentManager.beginTransaction()
                    // .add(R.id.item_detail_container, fragment)
                    .add(R.id.item_details, fragment)
                    .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> {
                    // This ID represents the Home or Up button. In the case of this
                    // activity, the Up button is shown. For
                    // more details, see the Navigation pattern on Android Design:
                    //
                    // http://developer.android.com/design/patterns/navigation.html#up-vs-back

                    navigateUpTo(Intent(this, ItemListActivity::class.java))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
}
