package com.dicoding.githubusers1.data.Model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class UserResponse(

	@field:SerializedName("total_count")
	val totalCount: Int,

	@field:SerializedName("incomplete_results")
	val incompleteResults: Boolean,

	@field:SerializedName("items")
	val items: MutableList<User>
)
@Parcelize
@Entity(tableName = "user")
data class User(

	@ColumnInfo(name = "login")
	@field:SerializedName("login")
	val login: String,

	@PrimaryKey
	@field:SerializedName("id")
	val id: Int,

	@ColumnInfo(name = "avatarUrl")
	@field:SerializedName("avatar_url")
	val avatarUrl: String,
) : Parcelable
