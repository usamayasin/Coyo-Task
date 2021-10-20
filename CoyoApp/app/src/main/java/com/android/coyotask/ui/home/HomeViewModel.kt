package com.android.coyotask.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.coyotask.data.DataState
import com.android.coyotask.data.usecase.FetchPostsUsecase
import com.android.coyotask.model.PostModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchPostsUsecase: FetchPostsUsecase,
) : ViewModel() {

    private var _uiState = MutableLiveData<HomeUiState>()
    var uiStateLiveData: LiveData<HomeUiState> = _uiState

    private var _postsList = MutableLiveData<List<PostModel>>()
    var postsListLiveData: LiveData<List<PostModel>> = _postsList

    private var start = 0

    init {
        fetchPosts(start)
    }

    fun loadMorePosts() {
        start += 10
        fetchPosts(start)
    }

    fun retry() {
        fetchPosts(start)
    }

    fun refresh(start: Int) {
        this.start = start
        fetchPosts(this.start)
    }

    private fun fetchPosts(start: Int) {
        _uiState.postValue(if (start == 0) LoadingState else LoadingNextPageState)
        viewModelScope.launch {
            fetchPostsUsecase.invoke(start = start).collect { dataState ->
                when (dataState) {
                    is DataState.Success -> {
                        if (start == 0) {
                            // first page
                            _uiState.postValue(ContentState)
                            _postsList.postValue(dataState.data)
                        } else {
                            // Data is not empty
                            if (dataState.data.isNotEmpty()) {
                                // Any other page
                                _uiState.postValue(ContentNextPageState)
                                val currentList = arrayListOf<PostModel>()
                                _postsList.value?.let { currentList.addAll(it) }
                                currentList.addAll(dataState.data)
                                _postsList.postValue(currentList)
                            } else
                                _uiState.postValue(EmptyState)
                        }
                    }
                    is DataState.Error -> {
                        if (start == 0) {
                            _uiState.postValue(ErrorState(dataState.message))
                        } else {
                            _uiState.postValue(ErrorNextPageState(dataState.message))
                        }
                    }
                }
            }
        }
    }
}
