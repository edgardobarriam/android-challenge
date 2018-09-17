package io.github.edgardobarriam.techkandroidchallenge.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import io.github.edgardobarriam.techkandroidchallenge.R
import io.github.edgardobarriam.techkandroidchallenge.server.Gallery
import kotlinx.android.synthetic.main.gallery_list_content.view.*
import java.text.SimpleDateFormat
import java.util.*

class GalleriesRecyclerViewAdapter(val context: Context,
                                   val listGalleries: List<Gallery>,
                                   val itemClick: (Gallery) -> Unit)
    : RecyclerView.Adapter<GalleriesRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.gallery_list_content, parent, false)

        return ViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gallery = listGalleries[position]

        holder.bindGallery(gallery)
    }

    fun convertToDateTime(timestamp: Long) : String {
        val calendar = Calendar.getInstance()
        val timeZone = TimeZone.getDefault()
        calendar.add(Calendar.MILLISECOND, timeZone.getOffset(calendar.timeInMillis))
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
        val currentTime = java.util.Date(timestamp * 1000)
        return dateFormat.format(currentTime)
    }

    override fun getItemCount(): Int {
        return listGalleries.size
    }

    inner class ViewHolder(view: View, val itemClick: (Gallery) -> Unit) : RecyclerView.ViewHolder(view) {

        fun bindGallery(gallery: Gallery) {
            with(gallery) {
                itemView.textView_gallery_title.text = title
                itemView.textView_gallery_datetime.text = convertToDateTime(datetime)
                itemView.setOnClickListener { itemClick(this) }

                // TODO: Refactor this
                if(images == null ) {
                    itemView.textView_gallery_description.text = description

                    when(type) {
                        "image/jpeg" ->Glide.with(context).load(link).into(itemView.imageView_gallery_preview)
                        "image/png" -> Glide.with(context).load(link).into(itemView.imageView_gallery_preview)
                        "image/gif" -> Glide.with(context).asGif().load(link).into(itemView.imageView_gallery_preview)
                        else -> itemView.imageView_gallery_preview.setImageResource(R.drawable.video_placeholder)
                    }

                } else {
                    val firstImage = images[0]
                    itemView.textView_gallery_description.text = firstImage.description

                    when(firstImage.type) {
                        "image/jpeg" -> Glide.with(context).load(firstImage.link).into(itemView.imageView_gallery_preview)
                        "image/png" -> Glide.with(context).load(firstImage.link).into(itemView.imageView_gallery_preview)
                        "image/gif" -> Glide.with(context).asGif().load(firstImage.link).into(itemView.imageView_gallery_preview)
                        else -> itemView.imageView_gallery_preview.setImageResource(R.drawable.video_placeholder)
                    }
                }

            }
        }

    }
}