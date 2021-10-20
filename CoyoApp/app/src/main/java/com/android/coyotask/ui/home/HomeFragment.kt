package com.android.coyotask.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.coyotask.R
import com.android.coyotask.adapters.PostsAdapter
import com.android.coyotask.base.BaseFragment
import com.android.coyotask.databinding.HomeFragmentBinding
import com.android.coyotask.utils.gone
import com.android.coyotask.utils.showSnack
import com.android.coyotask.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeFragmentBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> HomeFragmentBinding
        get() = HomeFragmentBinding::inflate

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var postsAdapter: PostsAdapter
    private var postsLayoutManager: RecyclerView.LayoutManager? = null


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupViews()
        initObservations()
    }

    private fun setupViews() {
        context?.let {
            // Posts RecyclerView
            this.postsAdapter = PostsAdapter { post, _ ->
                val bundle = bundleOf("post" to post)
                findNavController().navigate(
                    R.id.action_homeFragment_to_postDetailsFragment,
                    bundle
                )
            }
            postsAdapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            this.postsLayoutManager = LinearLayoutManager(requireContext())
            bi.recyclerPosts.layoutManager = postsLayoutManager
            bi.recyclerPosts.adapter = postsAdapter

            bi.nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, _, scrollY, _, _ ->
                if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                    viewModel.loadMorePosts()
                }
            }
        }
    }

    private fun initObservations() {
        viewModel.uiStateLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoadingState -> {
                    bi.recyclerPosts.gone()
                    bi.progressPosts.visible()
                }

                is LoadingNextPageState -> {
                    bi.progressPosts.visible()
                }

                is EmptyState -> {
                    bi.recyclerPosts.visible()
                    bi.progressPosts.gone()
                }

                is ContentState -> {
                    bi.recyclerPosts.visible()
                    bi.progressPosts.gone()
                }

                is ErrorState -> {
                    bi.progressPosts.gone()
                    bi.nestedScrollView.showSnack(
                        state.message,
                        getString(R.string.action_retry_str)
                    ) {
                        viewModel.retry()
                    }
                }

                is ErrorNextPageState -> {
                    bi.nestedScrollView.showSnack(
                        state.message,
                        getString(R.string.action_retry_str)
                    ) {
                        viewModel.retry()
                    }
                }
            }
        }

        viewModel.postsListLiveData.observe(viewLifecycleOwner) { posts ->
            println("List is -> ${posts.size}")
            postsAdapter.updateItems(posts)
        }
    }


}