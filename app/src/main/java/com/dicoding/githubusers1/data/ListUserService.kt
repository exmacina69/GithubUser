package com.dicoding.githubusers1.data

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.QueryMap
import com.dicoding.githubusers1.data.Model.User
import com.dicoding.githubusers1.data.Model.UserResponse
import com.dicoding.githubusers1.BuildConfig
import com.dicoding.githubusers1.data.Model.DetailUserResponse


interface ListUserService {

    @JvmSuppressWildcards
    @GET("users")
    suspend fun getUserList(@Header("Authorization") authorization: String = BuildConfig.TOKEN_GITHUB): MutableList<User>

    @JvmSuppressWildcards
    @GET("search/users")
    suspend fun getUserSearch(
        @QueryMap params: Map<String, Any>,
        @Header("Authorization") authorization: String = BuildConfig.TOKEN_GITHUB
    ): UserResponse

    @JvmSuppressWildcards
    @GET("users/{username}/following")
    suspend fun getUserFollowing(
        @Path("username") username: String,
        @Header("Authorization") authorization: String = BuildConfig.TOKEN_GITHUB,
    ): MutableList<User>

    @JvmSuppressWildcards
    @GET("users/{username}/followers")
    suspend fun getUserFollower(
        @Path("username") username: String,
        @Header("Authorization") authorization: String = BuildConfig.TOKEN_GITHUB
    ): MutableList<User>

    @JvmSuppressWildcards
    @GET("users/{username}")
    suspend fun getUserDetail(
        @Path("username") username: String,
        @Header("Authorization")authorization: String = BuildConfig.TOKEN_GITHUB,
    ): DetailUserResponse
}