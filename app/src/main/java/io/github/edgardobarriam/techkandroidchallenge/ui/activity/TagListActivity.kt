package io.github.edgardobarriam.techkandroidchallenge.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.github.edgardobarriam.techkandroidchallenge.R
import io.github.edgardobarriam.techkandroidchallenge.ui.fragment.TagImagesFragment

import io.github.edgardobarriam.techkandroidchallenge.server.ImgurApiService
import io.github.edgardobarriam.techkandroidchallenge.server.Tag
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_tag_list.*
import kotlinx.android.synthetic.main.tag_list_content.view.*
import kotlinx.android.synthetic.main.tag_list.*
import org.jetbrains.anko.toast

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [TagImagesActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class TagListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane = false

    private val imgurApiService by lazy {
        ImgurApiService.getInstance()
    }
    var disposable: Disposable? = null

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        fab.setOnClickListener { view ->
            // TODO: Search
        }

        if (tag_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }



        fetchTagsFromApi()
    }

    private fun fetchTagsFromApi() {
        disposable = imgurApiService.getDefaultTags()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {result -> setupRecyclerView(tag_list, result.data.tags)},
                                {error -> toast(error.message.toString())}
                        )
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, items : List<Tag>) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, items, twoPane)
    }

    class SimpleItemRecyclerViewAdapter(private val parentActivity: TagListActivity,
                                        private val values: List<Tag>,
                                        private val twoPane: Boolean) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as Tag
                if (twoPane) {
                    val fragment = TagImagesFragment().apply {
                        arguments = Bundle().apply {
                            putString(TagImagesFragment.ARG_TAG_DISPLAY_NAME, item.display_name)
                        }
                    }
                    parentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.tag_detail_container, fragment)
                            .commit()
                } else {
                    // Handheld
                    val intent = Intent(v.context, TagImagesActivity::class.java).apply {
                        putExtra(TagImagesFragment.ARG_TAG_DISPLAY_NAME, item.display_name)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.tag_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.tagDisplayName.text = item.display_name

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tagDisplayName: TextView = view.textview_tag_name
        }
    }
}
