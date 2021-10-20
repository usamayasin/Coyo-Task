package com.android.coyotask.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PostModel(
    @Expose val id: Int,
    @Expose val userId: Int,
    @Expose val title: String,
    @Expose val body: String
) : Parcelable
