package com.android.coyotask.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.coyotask.databinding.CommentItemLayoutBinding
import com.android.coyotask.model.CommentModel

class CommentsAdapter() :
    RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    private val commentItems: ArrayList<CommentModel> = arrayListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentsAdapter.CommentViewHolder {
        val binding = CommentItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(commentItems[position])
    }

    override fun getItemCount() = commentItems.size

    fun updateItems(CommentsList: List<CommentModel>) {
        commentItems.clear()
        commentItems.addAll(CommentsList)
        notifyDataSetChanged()
    }

    inner class CommentViewHolder(private val itemBinding: CommentItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(commentModel: CommentModel) {
            itemBinding.data = commentModel
        }
    }
}
