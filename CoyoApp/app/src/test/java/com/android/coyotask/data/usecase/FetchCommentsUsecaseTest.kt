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
class FetchCommentsUsecaseTest {

    @MockK
    private lateinit var repository: Repository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test invoking FetchCommentsUsecase gives list of comments`() = runBlocking {
        // Given
        val fetchCommentsUsecase = FetchCommentsUsecase(repository)
        val mockComments = MockTestUtil.getMockComments(10)

        // When
        coEvery { repository.getComments(any()) }
            .returns(flowOf(DataState.success(mockComments)))

        // Invoke
        val commentsListFlow = fetchCommentsUsecase(1)

        // Then
        MatcherAssert.assertThat(commentsListFlow, CoreMatchers.notNullValue())

        val commentsListDataState = commentsListFlow.first()
        MatcherAssert.assertThat(commentsListDataState, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(commentsListDataState, CoreMatchers.instanceOf(DataState.Success::class.java))

        val commentsList = (commentsListDataState as DataState.Success).data
        MatcherAssert.assertThat(commentsList, CoreMatchers.notNullValue())
        Assert.assertEquals(commentsList.size, mockComments.size)
    }
}