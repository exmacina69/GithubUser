package com.dicoding.githubusers1.utils

sealed class Hasil {
    data class Success<out T>(val data: T) : Hasil()
    data class Error(val exception: Throwable) : Hasil()
    data class Loading(val isLoading: Boolean) : Hasil()
}
