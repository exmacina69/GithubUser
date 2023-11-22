package com.dicoding.githubusers1.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dicoding.githubusers1.data.Model.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDb:RoomDatabase() {
    abstract fun userDao():UserDao
}