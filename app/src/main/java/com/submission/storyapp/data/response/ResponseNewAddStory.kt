package com.submission.storyapp.data.response

import com.google.gson.annotations.SerializedName

data class ResponseNewAddStory(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("error")
    val error: Boolean? = null

)