package io.github.edgardobarriam.techkandroidchallenge.ui.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import io.github.edgardobarriam.techkandroidchallenge.R
import io.github.edgardobarriam.techkandroidchallenge.server.Gallery

object ImageDisplayHelper {

    fun displayImage(gallery: Gallery, imageView: ImageView, context: Context) {
        with(gallery) {
            when(gallery.type) {
                "image/jpeg"-> Glide.with(context).load(link).into(imageView)
                "image/png" -> Glide.with(context).load(link).into(imageView)
                "image/gif" -> Glide.with(context).asGif().load(link).into(imageView)
                else -> imageView.setImageResource(R.drawable.not_available)
            }
        }
    }
}