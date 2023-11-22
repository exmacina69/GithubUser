package com.dicoding.githubusers1.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dicoding.githubusers1.data.ApiClient
import com.dicoding.githubusers1.data.Model.User
import com.dicoding.githubusers1.data.db.DbGithubUserModule
import com.dicoding.githubusers1.utils.Hasil
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DetailUserViewModel(private val db: DbGithubUserModule):ViewModel() {
    val resultUserDetail = MutableLiveData<Hasil>()
    val resultUserFollowers = MutableLiveData<Hasil>()
    val resultUserFollowing = MutableLiveData<Hasil>()
    val resultFavSuccess = MutableLiveData<Boolean>()
    val resultFavDelete = MutableLiveData<Boolean>()

    private var isFavorite = false

    class Factory(private val db: DbGithubUserModule) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = DetailUserViewModel(db) as T
    }

    fun getUserDetails(username: String){
        viewModelScope.launch {
            flow{
                val response = ApiClient
                    .listUserService
                    .getUserDetail(username)

                emit(response)
            }.onStart {
                resultUserDetail.value = Hasil.Loading(true)
            }.onCompletion {
                resultUserDetail.value = Hasil.Loading(false)
            }.catch {
                resultUserDetail.value = Hasil.Error(it)
            }.collect{
                resultUserDetail.value = Hasil.Success(it)
            }
        }
    }

    fun getUserFollowers(username: String){
        viewModelScope.launch {
            flow{
                val response = ApiClient
                    .listUserService
                    .getUserFollower(username)

                emit(response)
            }.onStart {
                resultUserFollowers.value = Hasil.Loading(true)
            }.onCompletion {
                resultUserFollowers.value = Hasil.Loading(false)
            }.catch {
                resultUserFollowers.value = Hasil.Error(it)
            }.collect{
                resultUserFollowers.value = Hasil.Success(it)
            }
        }
    }
    fun getUserFollowings(username: String){
        viewModelScope.launch {
            flow{
                val response = ApiClient
                    .listUserService
                    .getUserFollowing(username)

                emit(response)
            }.onStart {
                resultUserFollowing.value = Hasil.Loading(true)
            }.onCompletion {
                resultUserFollowing.value = Hasil.Loading(false)
            }.catch {
                resultUserFollowing.value = Hasil.Error(it)
            }.collect{
                resultUserFollowing.value = Hasil.Success(it)
            }
        }
    }

    fun setUserFav(item: User?){
        viewModelScope.launch {
            item?.let {
                if(isFavorite){
                    db.userGithubDao.delete(item)
                    resultFavDelete.value=false
                }else{
                    db.userGithubDao.insert(item)
                    resultFavSuccess.value=true
                }
            }
            isFavorite = !isFavorite
        }
    }

    fun findUserFav(id: Int, listenFav: () -> Unit) {
        viewModelScope.launch {
            val user = db.userGithubDao.findById(id)
            if (user != null) {
                listenFav()
                isFavorite = true
            }
        }
    }
}