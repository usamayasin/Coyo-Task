package com.android.coyotask.data.usecase

import com.android.coyotask.data.repository.Repository
import com.android.coyotask.utils.AppConstants
import javax.inject.Inject

class FetchPostsUsecase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(
        start: Int,
        limit: Int = AppConstants.Network.POSTS_PER_PAGE,
    ) = repository.loadPosts(
        start = start,
        limit = limit
    )
}
