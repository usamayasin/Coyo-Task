package com.android.coyotask.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CommentModel(
    @Expose var id: Int,
    @Expose var postId: Int,
    @Expose var name: String,
    @Expose var email: String,
    @Expose var body: String,
) : Parcelable
