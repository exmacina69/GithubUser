package com.dicoding.githubusers1.ui.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.githubusers1.ui.detail.fragmenthing.DetailUserFragment

class DetailUserAdapter(
    fa:FragmentActivity,
    private val fragment: MutableList<DetailUserFragment>
):FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = fragment.size
    override fun createFragment(position: Int): Fragment = fragment[position]
}