package com.android.coyotask.ui.postdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.coyotask.R
import com.android.coyotask.adapters.CommentsAdapter
import com.android.coyotask.base.BaseFragment
import com.android.coyotask.databinding.PostDetailsFragmentBinding
import com.android.coyotask.model.PostModel
import com.android.coyotask.utils.gone
import com.android.coyotask.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailsFragment : BaseFragment<PostDetailsFragmentBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> PostDetailsFragmentBinding
        get() = PostDetailsFragmentBinding::inflate

    private val viewModel: PostDetailsViewModel by viewModels()
    private lateinit var commentsAdapter: CommentsAdapter
    private var commentsLayoutManager: RecyclerView.LayoutManager? = null


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val post = arguments?.getParcelable<PostModel>("post")
        if (post == null) {
            findNavController().popBackStack()
            return
        }

        setupViews()
        initObservations()

        viewModel.initPostDetailsVM(post)
    }

    private fun setupViews() {
        context?.let {
            // Comments RecyclerView
            commentsAdapter = CommentsAdapter()
            commentsAdapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            commentsLayoutManager = LinearLayoutManager(requireContext())
            bi.recyclerComments.layoutManager = commentsLayoutManager
            bi.recyclerComments.adapter = commentsAdapter
        }

    }

    private fun initObservations() {

        viewModel.uiStateLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoadingState -> {
                    bi.progressPostDetails.visible()
                    bi.recyclerComments.gone()
                }

                is ContentState -> {
                    bi.progressPostDetails.gone()
                    bi.recyclerComments.visible()
                }

                is ErrorState -> {
                    bi.progressPostDetails.gone()
                    bi.recyclerComments.visible()
                    viewModel.retry()
                }
            }
        }


        viewModel.userModelLiveData.observe(viewLifecycleOwner) { user ->
            bi.tvPostedBy.text = getString(R.string.posted_by, user.name)
        }

        viewModel.postModelLiveData.observe(viewLifecycleOwner) { post ->
            bi.tvPostTitle.text = post.title
            bi.tvPostBody.text = post.body
        }

        viewModel.commentModelLiveData.observe(viewLifecycleOwner) { comments ->
            bi.tvCommentsCount.text = getString(R.string.comments, comments.size.toString())
            commentsAdapter.updateItems(comments)
        }
    }

}