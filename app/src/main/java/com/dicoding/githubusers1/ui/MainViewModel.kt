package com.dicoding.githubusers1.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.githubusers1.data.ApiClient
import com.dicoding.githubusers1.data.preference.PreferenceSettings
import com.dicoding.githubusers1.utils.Hasil
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MainViewModel(private val preferences: PreferenceSettings):ViewModel() {

    val resultUser = MutableLiveData<Hasil>()

    class Factory(private val preferences: PreferenceSettings) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            MainViewModel(preferences) as T
    }

    fun getTema() = preferences.getSettingsTheme().asLiveData()

    fun getGithubUser(){
        viewModelScope.launch {
                flow{
                    val response = ApiClient
                        .listUserService
                        .getUserList()

                    emit(response)
                }.onStart {
                    resultUser.value = Hasil.Loading(true)
                }.onCompletion {
                    resultUser.value = Hasil.Loading(false)
                }.catch {
                    resultUser.value = Hasil.Error(it)
                }.collect{
                    resultUser.value = Hasil.Success(it)
                }
            }
    }

    fun getGithubUserSearch(username:String){
        viewModelScope.launch {
            flow{
                val response = ApiClient
                    .listUserService
                    .getUserSearch(mapOf(
                        "q" to username
                    ))

                emit(response)
            }.onStart {
                resultUser.value = Hasil.Loading(true)
            }.onCompletion {
                resultUser.value = Hasil.Loading(false)
            }.catch {
                resultUser.value = Hasil.Error(it)
            }.collect{
                resultUser.value = Hasil.Success(it.items)
            }
        }
    }
}