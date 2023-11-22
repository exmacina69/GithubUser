package com.dicoding.githubusers1.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubusers1.R
import com.dicoding.githubusers1.data.db.DbGithubUserModule
import com.dicoding.githubusers1.databinding.ActivityFavoriteBinding
import com.dicoding.githubusers1.ui.MainAdapter
import com.dicoding.githubusers1.ui.detail.DetailUserActivity
import com.dicoding.githubusers1.ui.setting.SettingsActivity

class FavoriteUsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val adapter by lazy {
        MainAdapter {
            Intent(this, DetailUserActivity::class.java).apply {
                putExtra("item", it)
                startActivity(this)
            }
        }
    }

    private val viewModel by viewModels<FavoriteUsersViewModel> {
        FavoriteUsersViewModel.Factory(DbGithubUserModule(this))
    }

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.adapter = adapter

        progressBar = binding.progressBar
    }

    override fun onResume() {
        super.onResume()
        progressBar.visibility = View.VISIBLE

        viewModel.getFavUser().observe(this) { userList ->
            progressBar.visibility = View.GONE

            if (userList.isEmpty()) {
                binding.imageNoFavorite.visibility = View.VISIBLE
                binding.textNoFavorite.visibility = View.VISIBLE
                binding.rvFavorite.visibility = View.GONE
            } else {
                binding.imageNoFavorite.visibility = View.GONE
                binding.textNoFavorite.visibility = View.GONE
                binding.rvFavorite.visibility = View.VISIBLE
                adapter.setGithubUserData(userList)
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.btn_setting -> {
                Intent(this, SettingsActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
    
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}