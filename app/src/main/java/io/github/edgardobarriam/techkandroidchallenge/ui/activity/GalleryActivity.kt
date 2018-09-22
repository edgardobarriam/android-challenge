package io.github.edgardobarriam.techkandroidchallenge.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import io.github.edgardobarriam.techkandroidchallenge.R
import io.github.edgardobarriam.techkandroidchallenge.server.Comment
import io.github.edgardobarriam.techkandroidchallenge.server.Gallery
import io.github.edgardobarriam.techkandroidchallenge.server.ImgurApiService
import io.github.edgardobarriam.techkandroidchallenge.ui.adapter.CommentsRecyclerViewAdapter
import io.github.edgardobarriam.techkandroidchallenge.ui.util.ImageDisplayHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_gallery.*
import org.jetbrains.anko.toast

class GalleryActivity : AppCompatActivity() {
    val activity = this

    private val imgurApiService by lazy {
        ImgurApiService.getInstance()
    }
    var disposableRequest: Disposable? = null

    override fun onPause() {
        super.onPause()
        disposableRequest?.dispose()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.gallery)

        val gallery = intent.getParcelableExtra<Gallery>(ARG_GALLERY)

        displayGallery(gallery)
        loadComments(gallery)
    }

    private fun displayGallery(gallery: Gallery) {
        with(gallery) {
            textView_title.text = title
            textView_upvotes.text = ups.toString()
            textView_downvotes.text = downs.toString()
            textView_views.text = views.toString()
            textView_description.text = description
            ImageDisplayHelper.displayImage(this, imageView_image, activity)
        }
    }

    private fun loadComments(gallery: Gallery) {
        disposableRequest = imgurApiService.getGalleryComments(gallery.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {result ->
                            if(result.data.isEmpty()) {
                                toast("No comments!")
                            } else {
                                setupCommentsRecycler(result.data)
                            }
                        },
                        {error -> toast(error.message!!)}
                )
    }

    private fun setupCommentsRecycler(comments: List<Comment>) {
        recyclerView_comments.adapter = CommentsRecyclerViewAdapter(comments)
        recyclerView_comments.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
    }

    companion object {
        const val ARG_GALLERY = "arg_gallery"
    }
}
