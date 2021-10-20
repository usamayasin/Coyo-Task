package com.android.coyotask.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.android.coyotask.MainCoroutinesRule
import com.android.coyotask.MockTestUtil
import com.android.coyotask.data.DataState
import com.android.coyotask.data.usecase.FetchPostsUsecase
import com.android.coyotask.model.PostModel
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
class HomeViewModelTest {

    // Subject under test
    private lateinit var viewModel: HomeViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @MockK
    lateinit var fetchPostsUsecase: FetchPostsUsecase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test when HomeViewModel is initialized, posts are fetched`() = runBlocking {
        // Given
        val givenPosts = MockTestUtil.getMockPosts(2)
        val uiObserver = mockk<Observer<HomeUiState>>(relaxed = true)
        val postsListObserver = mockk<Observer<List<PostModel>>>(relaxed = true)

        // When
        coEvery { fetchPostsUsecase.invoke(any()) }
            .returns(flowOf(DataState.success(givenPosts)))

        // Invoke
        viewModel = HomeViewModel(fetchPostsUsecase)
        viewModel.uiStateLiveData.observeForever(uiObserver)
        viewModel.postsListLiveData.observeForever(postsListObserver)

        // Then
        coVerify(exactly = 1) { fetchPostsUsecase.invoke(any()) }
        verify { uiObserver.onChanged(match { it == ContentState }) }
        verify { postsListObserver.onChanged(match { it.size == givenPosts.size }) }
    }
}
