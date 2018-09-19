package io.github.edgardobarriam.techkandroidchallenge.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import io.github.edgardobarriam.techkandroidchallenge.R
import io.github.edgardobarriam.techkandroidchallenge.ui.fragment.TagGalleriesFragment
import kotlinx.android.synthetic.main.activity_tag_galleries.*

/**
 * An activity representing a single Tag detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [TagListActivity].
 */
class TagGalleriesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag_galleries)
        setSupportActionBar(detail_toolbar)

        val tagName = intent.getStringExtra(TagGalleriesFragment.ARG_TAG_DISPLAY_NAME)
        supportActionBar?.title = tagName

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            val fragment = TagGalleriesFragment().apply {
                arguments = Bundle().apply {
                    putString(TagGalleriesFragment.ARG_TAG_DISPLAY_NAME,
                            intent.getStringExtra(TagGalleriesFragment.ARG_TAG_DISPLAY_NAME))

                    putString(TagGalleriesFragment.ARG_TAG_NAME,
                            intent.getStringExtra(TagGalleriesFragment.ARG_TAG_NAME))
                }
            }

            supportFragmentManager.beginTransaction()
                    .add(R.id.tag_galleries_container, fragment)
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

                    navigateUpTo(Intent(this, TagListActivity::class.java))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
}
