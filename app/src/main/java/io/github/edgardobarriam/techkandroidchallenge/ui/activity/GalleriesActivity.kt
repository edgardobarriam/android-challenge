package io.github.edgardobarriam.techkandroidchallenge.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import io.github.edgardobarriam.techkandroidchallenge.R
import io.github.edgardobarriam.techkandroidchallenge.ui.fragment.GalleriesFragment
import kotlinx.android.synthetic.main.activity_tag_galleries.*

/**
 * An activity representing a single Tag detail screen. This
 * activity is only used on narrow width devices.
 */
class GalleriesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag_galleries)

        val tagName = intent.getStringExtra(GalleriesFragment.ARG_TAG_DISPLAY_NAME)

        setSupportActionBar(toolbar)
        supportActionBar?.title = tagName
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Show the Up button in the action bar.

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity (when rotating the screen).
        // In this case, the fragment will automatically be re-added to its container.
        // http://developer.android.com/guide/components/fragments.html
        if (savedInstanceState == null) {
            setupGalleriesFragment()
        }
    }

    private fun setupGalleriesFragment() {
        val fragment = GalleriesFragment().apply {
            arguments = Bundle().apply {
                putString(GalleriesFragment.ARG_TAG_DISPLAY_NAME,
                        intent.getStringExtra(GalleriesFragment.ARG_TAG_DISPLAY_NAME))

                putString(GalleriesFragment.ARG_TAG_NAME,
                        intent.getStringExtra(GalleriesFragment.ARG_TAG_NAME))
            }
        }

        supportFragmentManager.beginTransaction()
                .add(R.id.tag_galleries_container, fragment)
                .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> {
                    // This ID represents the Home or Up button. In the case of this
                    // activity, the Up button is shown.
                    // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                    navigateUpTo(Intent(this, TagsActivity::class.java))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
}
