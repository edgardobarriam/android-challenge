package io.github.edgardobarriam.techkandroidchallenge.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.github.edgardobarriam.techkandroidchallenge.R
import io.github.edgardobarriam.techkandroidchallenge.server.ImgurApiService
import io.github.edgardobarriam.techkandroidchallenge.server.Tag
import io.github.edgardobarriam.techkandroidchallenge.server.TagSearch
import io.github.edgardobarriam.techkandroidchallenge.ui.adapter.TagsRecyclerViewAdapter
import io.github.edgardobarriam.techkandroidchallenge.ui.fragment.TagGalleriesFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_tag_list.*
import kotlinx.android.synthetic.main.tag_list.*
import org.jetbrains.anko.*

/**
 * An activity representing a list of Tags. This activity
 * has different presentations for handset and tablet-size devices.
 *
 * On handsets, the activity presents a list of items, which when touched,
 * lead to a [TagGalleriesActivity].
 *
 * On tablets, the activity presents the list of Tags
 * and it's corresponding Galleries side-by-side using two vertical panes.
 */
class TagListActivity : AppCompatActivity() {

    /** Whether or not the activity is in two-pane mode (running on a tablet). */
    private var twoPane = false

    private val imgurApiService by lazy {
        ImgurApiService.getInstance()
    }
    private var disposableRequest: Disposable? = null


    override fun onPause() {
        super.onPause()
        disposableRequest?.dispose()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag_list)

        if (tag_galleries_container != null) {
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        setSupportActionBar(toolbar)
        toolbar.title = title

        fetchDefaultTags()

        floatingActionButton_search_tag.setOnClickListener { showTagSearchDialog() }
    }

    private fun fetchDefaultTags() {
        disposableRequest = imgurApiService.getDefaultTags().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {response ->
                    val defaultTagList = response.data.tags
                    setupTagsList(defaultTagList)
                },
                {error -> toast(error.message!!)}
            )
    }

    private fun setupTagsList(items : List<Tag>) {
        recyclerView_tag_list.adapter = TagsRecyclerViewAdapter(this, items, twoPane)
    }


    private fun showTagSearchDialog() {
        alert{ title = "Search a Tag"

            customView {
                val tagInput = editText() {hint = "Tag"}

                positiveButton("Search") {
                    if( tagInput.text.isNotBlank() ) {
                        fetchCustomTagGalleries(tagInput.text.toString())
                    }
                }

                negativeButton("Cancel") {}
            }

        }.show()
    }

    private fun fetchCustomTagGalleries(tag: String) {
        disposableRequest = imgurApiService.getTagGalleries(tag).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {response ->
                            response.data.items.forEach { it.fixGallery() }
                            loadTagGalleries(response.data,twoPane)
                        },
                        {error -> toast(error.message!!)}
                )
    }

    private fun loadTagGalleries(tagSearch: TagSearch, twoPane: Boolean) {
        if (twoPane) {
            val fragment = TagGalleriesFragment().apply {
                arguments = Bundle().apply {
                    putString(TagGalleriesFragment.ARG_TAG_DISPLAY_NAME, tagSearch.display_name)
                    putString(TagGalleriesFragment.ARG_TAG_NAME, tagSearch.name)
                }
            }
            this.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.tag_galleries_container, fragment)
                    .commit()
        } else {
            startActivity(intentFor<TagGalleriesActivity>(
                    TagGalleriesFragment.ARG_TAG_NAME to tagSearch.name,
                    TagGalleriesFragment.ARG_TAG_DISPLAY_NAME to tagSearch.display_name
            ))
        }
    }

}
