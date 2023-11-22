package com.dicoding.githubusers1.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubusers1.databinding.ActivityMainBinding
import com.dicoding.githubusers1.data.Model.User
import com.dicoding.githubusers1.utils.Hasil
import kotlinx.coroutines.*
import androidx.core.widget.addTextChangedListener
import com.dicoding.githubusers1.R
import com.dicoding.githubusers1.data.preference.PreferenceSettings
import com.dicoding.githubusers1.ui.detail.DetailUserActivity
import com.dicoding.githubusers1.ui.favorite.FavoriteUsersActivity
import com.dicoding.githubusers1.ui.setting.SettingsActivity

class MainActivity() : AppCompatActivity() {

    private var isUserSearching = false
    private var currentSearchQuery: String? = null
    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy {
        MainAdapter {
            Intent(this, DetailUserActivity::class.java).apply {
                putExtra("item", it)
                startActivity(this)
            }
        }
    }

    private val viewModel by viewModels<MainViewModel> {
        MainViewModel.Factory(PreferenceSettings(this))
    }

    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getTema().observe(this) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter

        binding.etQuery.addTextChangedListener { editable ->
            if (editable.toString().isNotEmpty()) {
                performSearch()
            } else {
                if (isUserSearching) {
                    isUserSearching = false
                    currentSearchQuery = null
                    viewModel.getGithubUser()
                }
            }
        }

        binding.etQuery.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else {
                false
            }
        }

        viewModel.resultUser.observe(this) {
            when (it) {
                is Hasil.Success<*> -> {
                    adapter.setGithubUserData(it.data as MutableList<User>)
                }
                is Hasil.Error -> {
                    Toast.makeText(this, it.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is Hasil.Loading -> {
                    binding.progressBar.isVisible = it.isLoading
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getGithubUser() // Pindahkan pemanggilan fungsi getGithubUser() ke onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.fav -> {
                Intent(this, FavoriteUsersActivity::class.java).apply {
                    startActivity(this)
                }
            }
            R.id.btn_setting -> {
                Intent(this, SettingsActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun performSearch() {
        val query = binding.etQuery.text.toString().trim()
        if (query.isNotEmpty()) {
            isUserSearching = true
            currentSearchQuery = query
            searchJob?.cancel()
            searchJob = CoroutineScope(Dispatchers.Main).launch {
                delay(300)
                if (query == currentSearchQuery) {
                    viewModel.getGithubUserSearch(query)
                }
            }
        }
    }
}
