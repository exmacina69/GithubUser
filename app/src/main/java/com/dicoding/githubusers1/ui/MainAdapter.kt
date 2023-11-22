package com.dicoding.githubusers1.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.dicoding.githubusers1.data.Model.User
import com.dicoding.githubusers1.databinding.UserListBinding

class MainAdapter(
    private val data: MutableList<User> = mutableListOf(),
    private val listener: (User) -> Unit
) :
    RecyclerView.Adapter<MainAdapter.UserGithubViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserGithubViewHolder =
        UserGithubViewHolder(
            UserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: UserGithubViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener(item)
        }
    }

    override fun getItemCount(): Int = data.size

    fun setGithubUserData(data: MutableList<User>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    class UserGithubViewHolder(private val v: UserListBinding) : RecyclerView.ViewHolder(v.root) {
        fun bind(item: User) {
            Glide.with(v.root.context)
                .load(item.avatarUrl)
                .transform(CircleCrop())
                .into(v.ivUser)
            v.tvUsername.text = item.login

            val githubLinkText = "https://github.com/${item.login}"
            v.tvGithubLink.text = githubLinkText
        }
    }
}
