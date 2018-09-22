package io.github.edgardobarriam.techkandroidchallenge.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.edgardobarriam.techkandroidchallenge.R
import io.github.edgardobarriam.techkandroidchallenge.server.Gallery
import io.github.edgardobarriam.techkandroidchallenge.server.ImgurApiService
import io.github.edgardobarriam.techkandroidchallenge.ui.activity.GalleryActivity
import io.github.edgardobarriam.techkandroidchallenge.ui.util.GallerySearch
import io.github.edgardobarriam.techkandroidchallenge.ui.adapter.GalleriesRecyclerViewAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_tag_galleries.*
import kotlinx.android.synthetic.main.fragment_tag_galleries_list.*
import org.jetbrains.anko.customView
import org.jetbrains.anko.editText
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.verticalLayout


/**
 * A fragment representing a list of Galleries related to a Tag screen.
 * This fragment is either contained in a [TagsActivity] in two-pane mode (on tablets)
 * or a [GalleriesActivity] on handsets.
 */
class GalleriesFragment : Fragment() {

    private val imgurApiService by lazy {
        ImgurApiService.getInstance()
    }
    var disposableRequest: Disposable? = null

    lateinit var imgurTag : String
    lateinit var listGalleries : List<Gallery>

    override fun onPause() {
        super.onPause()
        disposableRequest?.dispose()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if ( it.containsKey(ARG_TAG_DISPLAY_NAME) ) activity?.toolbar_layout?.title = it.getString(ARG_TAG_DISPLAY_NAME)
            if ( it.containsKey(ARG_TAG_NAME) ) imgurTag = it.getString(ARG_TAG_NAME)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_tag_galleries_list, container, false)

        fetchGalleries(imgurTag)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView_galleries.addItemDecoration(DividerItemDecoration(recyclerView_galleries.context, DividerItemDecoration.VERTICAL))
        button_search_galleries.setOnClickListener { showSearchDialog() }
    }

    private fun showSearchDialog() {
        alert { title = "Seach a Gallery"
            customView {
                verticalLayout {
                    val titleInput = editText{hint = "Title"}
                    val descriptionInput = editText{hint = "Description"}

                    positiveButton("Search") {
                        val title = titleInput.text.toString()
                        val description = descriptionInput.text.toString()

                        listGalleries = GallerySearch.search(listGalleries, title, description)

                        if(listGalleries.isNotEmpty()) {
                            setupGalleriesRecycler(listGalleries)
                        }
                    }

                    negativeButton("Cancel") {}
                }
            }
        }.show()
    }

    private fun fetchGalleries(tag: String) {
        disposableRequest = imgurApiService.getTagGalleries(tag).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result ->
                            listGalleries = result.data.items
                            listGalleries.forEach { it.fixGallery() }

                            setupGalleriesRecycler(listGalleries)
                        },
                        {error -> toast(error.message!!)}
                )
    }

    private fun setupGalleriesRecycler(data: List<Gallery>) {
        recyclerView_galleries.adapter = GalleriesRecyclerViewAdapter(context!!,data) {
            // onClickListener
            startActivity( intentFor<GalleryActivity>(GalleryActivity.ARG_GALLERY to it) )
        }

    }

    companion object {
        const val ARG_TAG_DISPLAY_NAME = "tag_display_name"
        const val ARG_TAG_NAME = "tag_name"
    }
}
