package com.android.coyotask

import com.android.coyotask.model.CommentModel
import com.android.coyotask.model.PostModel
import com.android.coyotask.model.UserModel

class MockTestUtil {
    companion object {
        fun getMockPosts(count: Int): List<PostModel> {
            return (0 until count).map {
                PostModel(
                    id = 1,
                    userId = 2,
                    title = "Mock title",
                    body = "Mock body.",
                )
            }
        }

        fun getMockComments(count: Int): List<CommentModel> {
            return (0 until count).map {
                CommentModel(
                    id = 1,
                    postId = 2,
                    name = "Mock name",
                    email = "mock@email.com",
                    body ="Mock body"
                )
            }
        }

        fun getMockUser(count: Int): List<UserModel> {
            return (0 until count).map {
                UserModel(
                    id = 1,
                    username = "Mock username",
                    name = "Mock name",
                    email = "mock@email.com",
                    phone ="12345678",
                    website = "www.mockweb.com"
                )
            }
        }
    }
}
