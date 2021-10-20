package com.android.coyotask.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.coyotask.databinding.PostItemLayoutBinding
import com.android.coyotask.model.PostModel

class PostsAdapter(val onPostSelected: (Post: PostModel, position: Int) -> Unit) :
    RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    private val postItems: ArrayList<PostModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(postItems[position], position)
    }

    override fun getItemCount() = postItems.size

    fun updateItems(PostsList: List<PostModel>) {
        postItems.clear()
        postItems.addAll(PostsList)
        notifyDataSetChanged()
    }

    inner class PostViewHolder(private val itemBinding: PostItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(postModel: PostModel, position: Int) {
            itemBinding.data = postModel
            itemBinding.cardPost.setOnClickListener{
                onPostSelected(postModel, position)
            }
        }
    }
}
