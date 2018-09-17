package io.github.edgardobarriam.techkandroidchallenge.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import io.github.edgardobarriam.techkandroidchallenge.R
import io.github.edgardobarriam.techkandroidchallenge.server.Gallery
import kotlinx.android.synthetic.main.activity_gallery.*

class GalleryActivity : AppCompatActivity() {
    val activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        setSupportActionBar(toolbar)

        val gallery = intent.getParcelableExtra<Gallery>(ARG_GALLERY)

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

    companion object {
        const val ARG_GALLERY = "arg_gallery"
    }
}
