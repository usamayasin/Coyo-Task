package com.android.coyotask.ui.postdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.coyotask.data.DataState
import com.android.coyotask.data.usecase.FetchCommentsUsecase
import com.android.coyotask.data.usecase.FetchUserInfoUsecase
import com.android.coyotask.model.CommentModel
import com.android.coyotask.model.PostModel
import com.android.coyotask.model.UserModel
import com.android.coyotask.ui.home.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    private val fetchUserInfoUsecase: FetchUserInfoUsecase,
    private val fetchCommentsUsecase: FetchCommentsUsecase,
) : ViewModel() {

    private var _uiState = MutableLiveData<PostDetailsUiState>()
    var uiStateLiveData: LiveData<PostDetailsUiState> = _uiState

    private var _userModel = MutableLiveData<UserModel>()
    var userLiveData: LiveData<UserModel> = _userModel

    private var _postModel = MutableLiveData<PostModel>()
    var postLiveData: LiveData<PostModel> = _postModel

    private var _commentModel = MutableLiveData<List<CommentModel>>()
    var commentLiveData: LiveData<List<CommentModel>> = _commentModel

    init {

    }

    fun initPostDetailsVM(post: PostModel) {
        _postModel.value = post
        fetchUserInfo(post.userId)
        fetchComments(post.id)
    }


    private fun fetchUserInfo(userId: Int) {
        _uiState.postValue(LoadingState)
        viewModelScope.launch {
            fetchUserInfoUsecase.invoke(userId = userId).collect { dataState ->
                when (dataState) {
                    is DataState.Success -> {
                        _uiState.postValue(ContentState)
                        _userModel.postValue(dataState.data[0])
                    }
                    is DataState.Error -> {
                        _uiState.postValue(ErrorState(dataState.message))
                    }
                }
            }
        }
    }

    private fun fetchComments(postId: Int) {
        _uiState.postValue(LoadingState)
        viewModelScope.launch {
            fetchCommentsUsecase.invoke(postId = postId).collect { dataState ->
                when (dataState) {
                    is DataState.Success -> {
                        _uiState.postValue(ContentState)
                        _commentModel.postValue(dataState.data)
                    }
                    is DataState.Error -> {
                        _uiState.postValue(ErrorState(dataState.message))
                    }
                }
            }
        }
    }

}
