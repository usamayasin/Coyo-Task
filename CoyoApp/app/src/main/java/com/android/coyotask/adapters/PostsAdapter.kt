package com.android.coyotask.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.coyotask.databinding.PostItemLayoutBinding
import com.android.coyotask.model.PostModel

class PostsAdapter(val onPostSelected: (Post: PostModel, position: Int) -> Unit) :
    RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(differ.currentList[position], position)
    }

    override fun getItemCount() = differ.currentList.size


    inner class PostViewHolder(private val itemBinding: PostItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(postModel: PostModel, position: Int) {
            itemBinding.apply {
                data = postModel
                cardPost.setOnClickListener{
                    onPostSelected(postModel, position)
                }
            }
        }
    }

    private val differCallBack  = object : DiffUtil.ItemCallback<PostModel, >() {

        override fun areItemsTheSame(oldItem: PostModel, newItem: PostModel, ): Boolean {
            return  oldItem.id== newItem.id
        }
        override fun areContentsTheSame(oldItem: PostModel, newItem: PostModel, ): Boolean {
            return  oldItem==newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallBack)
}
