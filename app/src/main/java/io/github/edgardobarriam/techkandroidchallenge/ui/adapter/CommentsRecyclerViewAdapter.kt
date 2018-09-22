package io.github.edgardobarriam.techkandroidchallenge.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.github.edgardobarriam.techkandroidchallenge.R
import io.github.edgardobarriam.techkandroidchallenge.server.Comment
import kotlinx.android.synthetic.main.comment_list_item.view.*

class CommentsRecyclerViewAdapter(val comments: List<Comment>) : RecyclerView.Adapter<CommentsRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.comment_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = comments.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val commentItem = comments[position]

        with (commentItem){
            holder.author.text = author
            holder.comment.text = comment
        }
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val author: TextView = view.textView_author
        val comment: TextView = view.textView_comment
    }
}