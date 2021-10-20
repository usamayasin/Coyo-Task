package com.android.coyotask.data.repository

import com.android.coyotask.data.DataState
import com.android.coyotask.model.CommentModel
import com.android.coyotask.model.PostModel
import com.android.coyotask.model.UserModel
import kotlinx.coroutines.flow.Flow

/**
 * Repository is an interface data layer to handle communication with any data source such as Server or local database.
 * @see [RepositoryImpl] for implementation of this class to utilize APIService.
 */
interface Repository {
    suspend fun loadPosts(start: Int, limit: Int): Flow<DataState<List<PostModel>>>
    suspend fun getUserInfo(userId: Int): Flow<DataState<List<UserModel>>>
    suspend fun getComments(postId: Int): Flow<DataState<List<CommentModel>>>
}
