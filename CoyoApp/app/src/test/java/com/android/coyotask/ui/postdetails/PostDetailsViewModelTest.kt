package com.android.coyotask.ui.postdetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.android.coyotask.MainCoroutinesRule
import com.android.coyotask.MockTestUtil
import com.android.coyotask.data.DataState
import com.android.coyotask.data.usecase.FetchCommentsUsecase
import com.android.coyotask.data.usecase.FetchUserInfoUsecase
import com.android.coyotask.model.CommentModel
import com.android.coyotask.model.PostModel
import com.android.coyotask.model.UserModel
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PostDetailsViewModelTest {

    // Subject under test
    private lateinit var viewModel: PostDetailsViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @MockK
    lateinit var fetchCommentsUsecase: FetchCommentsUsecase

    @MockK
    lateinit var fetchUserInfoUsecase: FetchUserInfoUsecase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test when method initPostDetailsVM is called, userinfo and comments are fetched`() = runBlocking {
        // Given
        val givenUsers = MockTestUtil.getMockUser(1)
        val uiObserver = mockk<Observer<PostDetailsUiState>>(relaxed = true)
        val usersListObserver = mockk<Observer<UserModel>>(relaxed = true)

        val givenComments = MockTestUtil.getMockComments(1)
        val commentsListObserver = mockk<Observer<List<CommentModel>>>(relaxed = true)

        val mockPost = MockTestUtil.getMockPosts(1)
        val postsListObserver = mockk<Observer<PostModel>>(relaxed = true)

        // When
        coEvery { fetchUserInfoUsecase.invoke(any()) }
            .returns(flowOf(DataState.success(givenUsers)))
        coEvery { fetchCommentsUsecase.invoke(any()) }
            .returns(flowOf(DataState.success(givenComments)))

        // Invoke
        viewModel = PostDetailsViewModel(fetchUserInfoUsecase,fetchCommentsUsecase)
        viewModel.initPostDetailsVM(mockPost[0])
        viewModel.uiStateLiveData.observeForever(uiObserver)
        viewModel.userLiveData.observeForever(usersListObserver)
        viewModel.commentLiveData.observeForever(commentsListObserver)
        viewModel.postLiveData.observeForever(postsListObserver)

        // Then
        coVerify(exactly = 1) { fetchUserInfoUsecase.invoke(any()) }
        verify { uiObserver.onChanged(match { it == ContentState }) }
        verify { usersListObserver.onChanged(match { it == givenUsers[0] }) }

        coVerify(exactly = 1) { fetchCommentsUsecase.invoke(any()) }
        verify { uiObserver.onChanged(match { it == ContentState }) }
        verify { commentsListObserver.onChanged(match { it.size == givenComments.size }) }

    }
}