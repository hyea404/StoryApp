package com.submission.storyapp.data.retrofit

import com.submission.storyapp.data.response.ResponseGetAllStory
import com.submission.storyapp.data.response.ResponseNewAddStory
import com.submission.storyapp.data.response.ResponseUserLogin
import com.submission.storyapp.data.response.ResponseUserRegister
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Part
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(

        @Field("name")
        name: String,

        @Field("email")
        email: String,

        @Field("password")
        password: String
    ): ResponseUserRegister

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email")
        email: String,

        @Field("password")
        password: String
    ): ResponseUserLogin


    @GET("stories")
    suspend fun getStories(
        @Header("Authorization")
        token: String,

        @Query("page")
        page: Int = 1,

        @Query("size")
        size: Int = 20
    ): ResponseGetAllStory

    @Multipart
    @POST("stories")
    suspend fun uploadStories(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null
    ): ResponseNewAddStory

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Header("Authorization") token: String,
        @Query("location") location: Int = 1
    ) : ResponseGetAllStory

}