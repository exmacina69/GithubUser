package com.dicoding.githubusers1.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dicoding.githubusers1.data.Model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Query("SELECT * FROM User")
    fun loadAll(): LiveData<MutableList<User>>

    @Query("SELECT * FROM User WHERE id LIKE :id LIMIT 1")
    fun findById(id: Int): User

    @Delete
    fun delete(user: User)
}