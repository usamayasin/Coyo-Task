package com.android.coyotask.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.coyotask.MainCoroutinesRule
import com.android.coyotask.MockTestUtil
import com.android.coyotask.data.DataState
import com.android.coyotask.data.remote.ApiService
import com.android.coyotask.utils.StringUtils
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import com.android.coyotask.data.remote.ApiUtil.successCall
import com.android.coyotask.model.PostModel
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import com.android.coyotask.data.remote.ApiResponse
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response

@RunWith(JUnit4::class)
class RepositoryImplTest {

    // Subject under test
    private lateinit var repository: RepositoryImpl

    @MockK
    private lateinit var apiService: ApiService

    @MockK
    private lateinit var stringUtils: StringUtils

    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test loadPosts() gives list of posts`() = runBlocking {
        // Given
        repository = RepositoryImpl(stringUtils, apiService)
        val givenPostsList = MockTestUtil.getMockPosts(10)
        val apiCall = successCall(givenPostsList)

        // When
        coEvery { apiService.loadPosts(any(), any()) }
            .returns(apiCall)

        // Invoke
        val apiResponseFlow = repository.loadPosts(0, 10)

        // Then
        MatcherAssert.assertThat(apiResponseFlow, CoreMatchers.notNullValue())

        val postsListDataState = apiResponseFlow.first()
        MatcherAssert.assertThat(postsListDataState, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(
            postsListDataState,
            CoreMatchers.instanceOf(DataState.Success::class.java)
        )

        val postsList = (postsListDataState as DataState.Success).data
        MatcherAssert.assertThat(postsList, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(postsList.size, CoreMatchers.`is`(givenPostsList.size))

        coVerify(exactly = 1) { apiService.loadPosts(any(), any()) }
        confirmVerified(apiService)
    }

    @Test
    fun `test loadPosts() gives empty list of posts`() = runBlocking {
        // Given
        repository = RepositoryImpl(stringUtils, apiService)
        val givenPostsList = MockTestUtil.getMockPosts(0)
        val apiCall = successCall(givenPostsList)

        // When
        coEvery { apiService.loadPosts(any(), any()) }
            .returns(apiCall)

        // Invoke
        val apiResponseFlow = repository.loadPosts(0, 10)

        // Then
        MatcherAssert.assertThat(apiResponseFlow, CoreMatchers.notNullValue())

        val postsListDataState = apiResponseFlow.first()
        MatcherAssert.assertThat(postsListDataState, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(
            postsListDataState,
            CoreMatchers.instanceOf(DataState.Success::class.java)
        )

        val postsList = (postsListDataState as DataState.Success).data
        MatcherAssert.assertThat(postsList, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(postsList.size, CoreMatchers.`is`(givenPostsList.size))

        coVerify(exactly = 1) { apiService.loadPosts(any(), any()) }
        confirmVerified(apiService)
    }

    @Test
    fun `test loadPosts() throws exception`() = runBlocking {
        // Given
        repository = RepositoryImpl(stringUtils, apiService)
        val givenMessage = "Test Error Message"
        val exception = Exception(givenMessage)
        val apiResponse = ApiResponse.exception<List<PostModel>>(exception)

        // When
        coEvery { apiService.loadPosts(any(), any()) }
            .returns(apiResponse)
        coEvery { stringUtils.somethingWentWrong() }
            .returns(givenMessage)

        // Invoke
        val apiResponseFlow = repository.loadPosts(0, 10)

        // Then
        MatcherAssert.assertThat(apiResponseFlow, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(apiResponseFlow.count(), CoreMatchers.`is`(1))

        val apiResponseDataState = apiResponseFlow.first()
        MatcherAssert.assertThat(apiResponseDataState, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(apiResponseDataState, CoreMatchers.instanceOf(DataState.Error::class.java))

        val errorMessage = (apiResponseDataState as DataState.Error).message
        MatcherAssert.assertThat(errorMessage, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(errorMessage, CoreMatchers.equalTo(givenMessage))

        coVerify(atLeast = 1) { apiService.loadPosts(any(), any()) }
        confirmVerified(apiService)
    }
}