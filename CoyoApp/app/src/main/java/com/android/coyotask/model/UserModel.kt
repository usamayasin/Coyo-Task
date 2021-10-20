package com.android.coyotask.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel(
    @Expose var id: Int,
    @Expose var name: String,
    @Expose var username: String,
    @Expose var email: String,
    @Expose var phone: String,
    @Expose var website: String,
) : Parcelable
