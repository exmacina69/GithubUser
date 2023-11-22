package com.dicoding.githubusers1.data.db

import android.content.Context
import androidx.room.Room

class DbGithubUserModule(private val context: Context) {
    private val db = Room.databaseBuilder(context, AppDb::class.java, "usergithub.db")
        .allowMainThreadQueries()
        .build()

    val userGithubDao = db.userDao()
}