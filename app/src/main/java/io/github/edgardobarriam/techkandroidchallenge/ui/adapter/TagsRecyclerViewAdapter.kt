package io.github.edgardobarriam.techkandroidchallenge.ui.adapter

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.github.edgardobarriam.techkandroidchallenge.R
import io.github.edgardobarriam.techkandroidchallenge.server.Tag
import io.github.edgardobarriam.techkandroidchallenge.ui.activity.TagGalleriesActivity
import io.github.edgardobarriam.techkandroidchallenge.ui.activity.TagListActivity
import io.github.edgardobarriam.techkandroidchallenge.ui.fragment.TagGalleriesFragment
import kotlinx.android.synthetic.main.tag_list_item.view.*

class TagsRecyclerViewAdapter(private val parentActivity: TagListActivity,
                              private val values: List<Tag>,
                              private val twoPane: Boolean) :
        RecyclerView.Adapter<TagsRecyclerViewAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as Tag
            if (twoPane) {
                val fragment = TagGalleriesFragment().apply {
                    arguments = Bundle().apply {
                        putString(TagGalleriesFragment.ARG_TAG_DISPLAY_NAME, item.display_name)
                        putString(TagGalleriesFragment.ARG_TAG_NAME, item.name)
                    }
                }
                parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.tag_galleries_container, fragment)
                        .commit()
            } else {
                // Handheld
                val intent = Intent(v.context, TagGalleriesActivity::class.java).apply {
                    putExtra(TagGalleriesFragment.ARG_TAG_DISPLAY_NAME, item.display_name)
                    putExtra(TagGalleriesFragment.ARG_TAG_NAME, item.name)
                }
                v.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.tag_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.tagDisplayName.text = item.display_name

        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount() = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tagDisplayName: TextView = view.textView_name
    }
}