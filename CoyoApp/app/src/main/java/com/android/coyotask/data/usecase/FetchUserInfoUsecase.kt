package com.android.coyotask.data.usecase

import com.android.coyotask.data.repository.Repository
import javax.inject.Inject

class FetchUserInfoUsecase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(
        userId: Int,
    ) = repository.getUserInfo(
        userId = userId,
    )
}
