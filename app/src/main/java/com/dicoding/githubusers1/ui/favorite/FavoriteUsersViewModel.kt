package com.dicoding.githubusers1.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.githubusers1.data.db.DbGithubUserModule

class FavoriteUsersViewModel(private val dbModule: DbGithubUserModule) : ViewModel() {

    class Factory(private val db: DbGithubUserModule) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = FavoriteUsersViewModel(db) as T
    }
    fun getFavUser() = dbModule.userGithubDao.loadAll()
}