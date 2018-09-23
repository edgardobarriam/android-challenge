package io.github.edgardobarriam.techkandroidchallenge.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.edgardobarriam.techkandroidchallenge.R
import io.github.edgardobarriam.techkandroidchallenge.server.Gallery
import io.github.edgardobarriam.techkandroidchallenge.ui.util.ImageDisplayHelper
import io.github.edgardobarriam.techkandroidchallenge.ui.util.TimestampFormatter
import kotlinx.android.synthetic.main.gallery_list_item.view.*

class GalleriesRecyclerViewAdapter(val context: Context,
                                   private val listGalleries: List<Gallery>,
                                   private val itemClick: (Gallery) -> Unit)
    : RecyclerView.Adapter<GalleriesRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.gallery_list_item, parent, false)

        return ViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gallery = listGalleries[position]
        holder.bindGallery(gallery)
    }

    override fun getItemCount() = listGalleries.size

    inner class ViewHolder(view: View, val itemClick: (Gallery) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bindGallery(gallery: Gallery) {
            with(gallery) {
                itemView.textView_title.text = title
                itemView.textView_gallery_datetime.text = TimestampFormatter.timestampToDateTimeString(datetime)
                itemView.textView_description.text = description
                ImageDisplayHelper.displayImage(this, itemView.imageView_image, context)
                itemView.setOnClickListener { itemClick(this) }
            }
        }

    }
}