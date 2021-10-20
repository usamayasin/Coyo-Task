package com.android.coyotask.data.repository

import androidx.annotation.WorkerThread
import com.android.coyotask.data.DataState
import com.android.coyotask.data.remote.*
import com.android.coyotask.model.CommentModel
import com.android.coyotask.model.PostModel
import com.android.coyotask.model.UserModel
import com.android.coyotask.utils.StringUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

/**
 * This is an implementation of [Repository] to handle communication with [ApiService] server.
 */
class RepositoryImpl @Inject constructor(
    private val stringUtils: StringUtils,
    private val apiService: ApiService
) : Repository {

    @WorkerThread
    override suspend fun loadPosts(
        start: Int,
        limit: Int
    ): Flow<DataState<List<PostModel>>> {
        return flow {
            apiService.loadPosts(start, limit).apply {
                this.onSuccessSuspend {
                    data?.let {
                        emit(DataState.success(it))
                    }
                }
                // handle the case when the API request gets an error response.
                // e.g. internal server error.
            }.onErrorSuspend {
                emit(DataState.error<List<PostModel>>(message()))

                // handle the case when the API request gets an exception response.
                // e.g. network connection error.
            }.onExceptionSuspend {
                if (this.exception is IOException) {
                    emit(DataState.error<List<PostModel>>(stringUtils.noNetworkErrorMessage()))
                } else {
                    emit(DataState.error<List<PostModel>>(stringUtils.somethingWentWrong()))
                }
            }
        }

    }

    @WorkerThread
    override suspend fun getUserInfo(userId: Int): Flow<DataState<List<UserModel>>> {
        return flow {
            apiService.getUserInfo(userId).apply {
                this.onSuccessSuspend {
                    data?.let {
                        emit(DataState.success(it))
                    }
                }
                // handle the case when the API request gets an error response.
                // e.g. internal server error.
            }.onErrorSuspend {
                emit(DataState.error<List<UserModel>>(message()))

                // handle the case when the API request gets an exception response.
                // e.g. network connection error.
            }.onExceptionSuspend {
                if (this.exception is IOException) {
                    emit(DataState.error<List<UserModel>>(stringUtils.noNetworkErrorMessage()))
                } else {
                    emit(DataState.error<List<UserModel>>(stringUtils.somethingWentWrong()))
                }
            }
        }
    }

    @WorkerThread
    override suspend fun getComments(postId: Int): Flow<DataState<List<CommentModel>>> {
        return flow {
            apiService.getComments(postId).apply {
                this.onSuccessSuspend {
                    data?.let {
                        emit(DataState.success(it))
                    }
                }
                // handle the case when the API request gets an error response.
                // e.g. internal server error.
            }.onErrorSuspend {
                emit(DataState.error<List<CommentModel>>(message()))

                // handle the case when the API request gets an exception response.
                // e.g. network connection error.
            }.onExceptionSuspend {
                if (this.exception is IOException) {
                    emit(DataState.error<List<CommentModel>>(stringUtils.noNetworkErrorMessage()))
                } else {
                    emit(DataState.error<List<CommentModel>>(stringUtils.somethingWentWrong()))
                }
            }
        }
    }
}
