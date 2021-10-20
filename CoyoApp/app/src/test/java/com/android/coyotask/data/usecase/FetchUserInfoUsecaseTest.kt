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
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FetchUserInfoUsecaseTest {

    @MockK
    private lateinit var repository: Repository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test invoking FetchUserInfoUsercase gives list of user`() = runBlocking {
        // Given
        val fetchUserInfoUsecase = FetchUserInfoUsecase(repository)
        val givenUser = MockTestUtil.getMockUser(1)

        // When
        coEvery { repository.getUserInfo(any()) }
            .returns(flowOf(DataState.success(givenUser)))

        // Invoke
        val usersListFlow = fetchUserInfoUsecase(1)

        // Then
        MatcherAssert.assertThat(usersListFlow, CoreMatchers.notNullValue())

        val userListDataState = usersListFlow.first()
        MatcherAssert.assertThat(userListDataState, CoreMatchers.notNullValue())
        MatcherAssert.assertThat(userListDataState, CoreMatchers.instanceOf(DataState.Success::class.java))

        val usersList = (userListDataState as DataState.Success<*>).data
        MatcherAssert.assertThat(usersList, CoreMatchers.notNullValue())
    }
}