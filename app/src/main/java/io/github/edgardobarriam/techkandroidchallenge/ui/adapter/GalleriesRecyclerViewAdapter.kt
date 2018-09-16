package io.github.edgardobarriam.techkandroidchallenge.ui.adapter

import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.github.edgardobarriam.techkandroidchallenge.R
import io.github.edgardobarriam.techkandroidchallenge.server.Gallery
import kotlinx.android.synthetic.main.gallery_list_content.view.*
import java.util.*

class GalleriesRecyclerViewAdapter(val listGalleries: List<Gallery>)
    : RecyclerView.Adapter<GalleriesRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.gallery_list_content, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listGalleries[position]

        holder.title.text = item.title

        if(item.images == null) {
            holder.description.text = item.description
        } else {
            holder.description.text = item.images[0].description
        }

        //TODO holder.image PICASSO/GLIDE Check if type in gallery or array
        holder.datetime.text = item.datetime.toString()
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