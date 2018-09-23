package io.github.edgardobarriam.techkandroidchallenge.ui.adapter

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.github.edgardobarriam.techkandroidchallenge.R
import io.github.edgardobarriam.techkandroidchallenge.server.Tag
import io.github.edgardobarriam.techkandroidchallenge.ui.activity.GalleriesActivity
import io.github.edgardobarriam.techkandroidchallenge.ui.activity.TagsActivity
import io.github.edgardobarriam.techkandroidchallenge.ui.fragment.GalleriesFragment
import kotlinx.android.synthetic.main.tag_list_item.view.*

class TagsRecyclerViewAdapter(private val parentActivity: TagsActivity,
                              private val listTags: List<Tag>,
                              private val twoPane: Boolean) :
        RecyclerView.Adapter<TagsRecyclerViewAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as Tag
            if (twoPane) {
                setupGalleriesFragment(item)
            } else {
                startGalleriesActivity(item, v)
            }
        }
    }

    private fun setupGalleriesFragment(item: Tag) {
        val fragment = GalleriesFragment().apply {
            arguments = Bundle().apply {
                putString(GalleriesFragment.ARG_TAG_DISPLAY_NAME, item.display_name)
                putString(GalleriesFragment.ARG_TAG_NAME, item.name)
            }
        }
        parentActivity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.tag_galleries_container, fragment)
                .commit()
    }

    private fun startGalleriesActivity(item: Tag, view: View) {
        val intent = Intent(view.context, GalleriesActivity::class.java).apply {
            putExtra(GalleriesFragment.ARG_TAG_DISPLAY_NAME, item.display_name)
            putExtra(GalleriesFragment.ARG_TAG_NAME, item.name)
        }
        view.context.startActivity(intent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.tag_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listTags[position]

        holder.tagDisplayName.text = item.display_name
        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount() = listTags.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tagDisplayName: TextView = view.textView_name
    }
}