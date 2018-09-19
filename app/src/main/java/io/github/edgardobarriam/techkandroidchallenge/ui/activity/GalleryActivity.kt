package io.github.edgardobarriam.techkandroidchallenge.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import com.bumptech.glide.Glide
import io.github.edgardobarriam.techkandroidchallenge.R
import io.github.edgardobarriam.techkandroidchallenge.server.Comment
import io.github.edgardobarriam.techkandroidchallenge.server.Gallery
import io.github.edgardobarriam.techkandroidchallenge.server.ImgurApiService
import io.github.edgardobarriam.techkandroidchallenge.ui.adapter.CommentsRecyclerViewAdapter
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
    var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Gallery"

        val gallery = intent.getParcelableExtra<Gallery>(ARG_GALLERY)

        displayGallery(gallery)
        loadComments(gallery)

    }

    fun displayGallery(gallery: Gallery) {
        //TODO: Refactor this
        with(gallery) {
            textView_gallery_title.text = title
            textView_upvotes.text = ups.toString()
            textView_downvotes.text = downs.toString()
            textView_views.text = views.toString()
            textView_gallery_description.text = description

            when(type) {
                "image/jpeg" ->Glide.with(activity).load(link).into(imageView_image)
                "image/png" -> Glide.with(activity).load(link).into(imageView_image)
                "image/gif" -> Glide.with(activity).asGif().load(link).into(imageView_image)
                else -> imageView_image.setImageResource(R.drawable.not_available)
            }

        }
    }

    fun loadComments(gallery: Gallery) {
        disposable = imgurApiService.getGalleryComments(gallery.id)
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
                        {error -> toast(error.message.toString())}
                )
    }

    fun setupCommentsRecycler(comments: List<Comment>) {
        recyclerView_gallery_comments.adapter = CommentsRecyclerViewAdapter(comments)
        recyclerView_gallery_comments.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
    }

    companion object {
        const val ARG_GALLERY = "arg_gallery"
    }
}
