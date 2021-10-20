package com.android.coyotask.ui.postdetails

sealed class PostDetailsUiState

object LoadingState : PostDetailsUiState()
object LoadingNextPageState : PostDetailsUiState()
object ContentState : PostDetailsUiState()
object ContentNextPageState : PostDetailsUiState()
object EmptyState : PostDetailsUiState()
class ErrorState(val message: String) : PostDetailsUiState()
class ErrorNextPageState(val message: String) : PostDetailsUiState()
