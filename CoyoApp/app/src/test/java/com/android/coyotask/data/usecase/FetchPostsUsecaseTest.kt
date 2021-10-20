package com.android.coyotask.data.usecase

import com.android.coyotask.MockTestUtil
import com.android.coyotask.data.DataState
import com.android.coyotask.data.repository.Repository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FetchPostsUsecaseTest {

    @MockK
    private lateinit var repository: Repository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test invoking FetchPostsUsecase gives list of posts`() = runBlocking {
        // Given
        val fetchPostsUsecase = FetchPostsUsecase(repository)
        val givenPosts = MockTestUtil.getMockPosts(10)

        // When
        coEvery { repository.loadPosts(any(), any()) }
            .returns(flowOf(DataState.success(givenPosts)))

        // Invoke
        val postsListFlow = fetchPostsUsecase(0, 10)

        // Then
        MatcherAssert.assertThat(postsListFlow, CoreMatchers.notNullValue())

        val postsListDataState = postsListFlow.first()
        MatcherAssert.assertThat(postsListDataState, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(postsListDataState, CoreMatchers.instanceOf(DataState.Success::class.java))

        val postsList = (postsListDataState as DataState.Success).data
        MatcherAssert.assertThat(postsList, CoreMatchers.notNullValue())
        Assert.assertEquals(postsList.size, givenPosts.size)
    }
}
