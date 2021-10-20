package com.android.coyotask.data.usecase

import com.android.coyotask.data.repository.Repository
import javax.inject.Inject

class FetchCommentsUsecase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(
        postId: Int,
    ) = repository.getComments(
        postId = postId,
    )
}
