package io.github.edgardobarriam.techkandroidchallenge.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.github.edgardobarriam.techkandroidchallenge.R
import io.github.edgardobarriam.techkandroidchallenge.dummy.DummyContent
import io.github.edgardobarriam.techkandroidchallenge.server.ImgurApiService
import io.github.edgardobarriam.techkandroidchallenge.ui.adapter.GalleriesRecyclerViewAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_tag_galleries.*
import kotlinx.android.synthetic.main.fragment_tag_galleries_list.*
import kotlinx.android.synthetic.main.tag_list.*
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import io.github.edgardobarriam.techkandroidchallenge.server.Gallery


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_TAG_DISPLAY_NAME)) activity?.toolbar_layout?.title = it.getString(ARG_TAG_DISPLAY_NAME)

            if (it.containsKey(ARG_TAG_NAME)) imgurTag = it.getString(ARG_TAG_NAME)


        }

    }

    fun fetchGalleries(tag: String) {
        disposable = imgurApiService.getTagGalleries(tag)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result -> setupGalleriesRecycler(list_tag_galleries, result.data)},
                        {error -> Toast.makeText(context,error.message,Toast.LENGTH_LONG).show()}
                )
    }

    fun setupGalleriesRecycler(recycler: RecyclerView, data: List<Gallery>) {
        list_tag_galleries.adapter = GalleriesRecyclerViewAdapter(context!!,data)
        list_tag_galleries.addItemDecoration(DividerItemDecoration(list_tag_galleries.context, DividerItemDecoration.VERTICAL))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_tag_galleries_list, container, false)

        fetchGalleries(imgurTag)
        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_TAG_DISPLAY_NAME = "tag_display_name"
        const val ARG_TAG_NAME = "tag_name"
    }
}
