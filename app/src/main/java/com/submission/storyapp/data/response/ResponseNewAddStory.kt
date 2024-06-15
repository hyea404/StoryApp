package com.submission.storyapp.data.response

import com.google.gson.annotations.SerializedName

data class ResponseNewAddStory(

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)