package io.github.edgardobarriam.techkandroidchallenge.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import io.github.edgardobarriam.techkandroidchallenge.R
import io.github.edgardobarriam.techkandroidchallenge.server.Gallery
import kotlinx.android.synthetic.main.gallery_list_content.view.*
import org.jetbrains.anko.imageResource
import java.util.*

class GalleriesRecyclerViewAdapter(val context: Context, val listGalleries: List<Gallery>)
    : RecyclerView.Adapter<GalleriesRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.gallery_list_content, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listGalleries[position]

        holder.title.text = item.title


        /*TODO: Refactor this, urgently: move responsibilities to different classes

          TODO: Check if image and type is either in galllery or on images array
          TODO: Check content type (Video, image)
         */
        if(item.images == null) {
            // Single image (?)
            holder.description.text = item.description

            when(item.type) {
                "image/jpeg" -> Glide.with(context).load(item.link).into(holder.image)
                "image/png" -> Glide.with(context).load(item.link).into(holder.image)
                "image/gif" -> Glide.with(context).asGif().load(item.link).into(holder.image)
                "video/mp4" -> holder.image.setImageResource(R.drawable.video)
            }

        } else {
            // Gallery
            holder.description.text = item.images[0].description

            when(item.images[0].type) {
                "image/jpeg" -> Glide.with(context).load(item.images[0].link).into(holder.image)
                "image/png" -> Glide.with(context).load(item.images[0].link).into(holder.image)
                "image/gif" -> Glide.with(context).asGif().load(item.images[0].link).into(holder.image)
                "video/mp4" -> holder.image.setImageResource(R.drawable.video)
            }
        }

        holder.datetime.text = item.datetime.toString() // TODO: display this timestamp properly
    }


    override fun getItemCount(): Int {
        return listGalleries.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.textView_gallery_title
        val description: TextView = view.textView_gallery_description
        val image: ImageView = view.imageView_gallery_preview
        val datetime: TextView = view.textView_gallery_datetime
    }
}