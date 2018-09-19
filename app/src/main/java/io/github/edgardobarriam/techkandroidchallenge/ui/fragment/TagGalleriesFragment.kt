package io.github.edgardobarriam.techkandroidchallenge.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.edgardobarriam.techkandroidchallenge.R
import io.github.edgardobarriam.techkandroidchallenge.server.Gallery
import io.github.edgardobarriam.techkandroidchallenge.server.ImgurApiService
import io.github.edgardobarriam.techkandroidchallenge.ui.activity.GalleryActivity
import io.github.edgardobarriam.techkandroidchallenge.ui.adapter.GalleriesRecyclerViewAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_tag_galleries.*
import kotlinx.android.synthetic.main.fragment_tag_galleries_list.*
import org.jetbrains.anko.customView
import org.jetbrains.anko.editText
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.verticalLayout


/**
 * A fragment representing a list of Galleries related to a Tag screen.
 * This fragment is either contained in a [TagListActivity]
 * in two-pane mode (on tablets) or a [TagGalleriesActivity]
 * on handsets.
 */
class TagGalleriesFragment : Fragment() {

    private val imgurApiService by lazy {
        ImgurApiService.getInstance()
    }
    var disposable: Disposable? = null
    var imgurTag : String = ""
    var listGalleries : List<Gallery>? = null

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

        button_search_galleries.onClick {

            alert {
                title = "Seach a Gallery"
                customView {

                    verticalLayout {
                        val inputTitle = editText{hint = "Title"}
                        val inputDescription = editText{hint= "Description"}

                        positiveButton("Search") {
                            val result = searchGallery(listGalleries!!, inputTitle.text.toString(), inputDescription.text.toString())
                            setupGalleriesRecycler(list_tag_galleries, result)
                        }

                        negativeButton("Cancel") {}
                    }
                }
            }.show()
        }

    }

    //TODO: Needs refactor and probably move to another file
    fun searchGallery(list: List<Gallery>, title: String, description: String) : List<Gallery> {
        return list.filter {
            it.title.contains(title, true)
            && it.description!!.contains(description,true)
        }
    }

    fun fetchGalleries(tag: String) {
        disposable = imgurApiService.getTagGalleries(tag)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result ->
                            listGalleries = result.data.items
                            setupGalleriesRecycler(list_tag_galleries, listGalleries!!)
                        },
                        {error -> toast(error.message!!)}
                )
    }

    fun setupGalleriesRecycler(recycler: RecyclerView, data: List<Gallery>) {
        recycler.adapter = GalleriesRecyclerViewAdapter(context!!,data) {
            // onClick
            startActivity( intentFor<GalleryActivity>(GalleryActivity.ARG_GALLERY to it) )
        }

        recycler.addItemDecoration(DividerItemDecoration(list_tag_galleries.context, DividerItemDecoration.VERTICAL))
    }

    companion object {
        const val ARG_TAG_DISPLAY_NAME = "tag_display_name"
        const val ARG_TAG_NAME = "tag_name"
    }
}
