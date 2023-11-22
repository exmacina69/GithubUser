package com.dicoding.githubusers1.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.dicoding.githubusers1.R
import com.dicoding.githubusers1.data.Model.DetailUserResponse
import com.dicoding.githubusers1.data.Model.User
import com.dicoding.githubusers1.data.db.DbGithubUserModule
import com.dicoding.githubusers1.databinding.ActivityDetailBinding
import com.dicoding.githubusers1.ui.detail.fragmenthing.DetailUserFragment
import com.dicoding.githubusers1.ui.setting.SettingsActivity
import com.dicoding.githubusers1.utils.Hasil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailUserViewModel> {
        DetailUserViewModel.Factory(DbGithubUserModule(this))
    }

    private fun FloatingActionButton.changeIconColor(colorResId: Int) {
        imageTintList = ContextCompat.getColorStateList(context, colorResId)
    }

    private fun setupTabLayoutAndViewPager(titles: List<String>, username: String) {
        TabLayoutMediator(binding.tlTab, binding.viewpager) { tab, position ->
            tab.text = titles[position]
        }.attach()

        binding.tlTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    viewModel.getUserFollowers(username)
                } else {
                    viewModel.getUserFollowings(username)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupViewModelObservers(username: String, item: User?) {
        viewModel.resultUserDetail.observe(this) { result ->
            when (result) {
                is Hasil.Success<*> -> {
                    val user = result.data as DetailUserResponse
                    with(binding) {
                        tvNama.text = user.name
                        tvIduser.text = user.login
                        Glide.with(this@DetailUserActivity)
                            .load(user.avatarUrl)
                            .transform(CircleCrop())
                            .into(ivFoto)

                        val followersText = resources.getString(R.string.followers_label, user.followers)
                        val followingText = resources.getString(R.string.following_label, user.following)

                        tvFollowers.text = followersText
                        tvFollowing.text = followingText

                        val githubLinkText = "https://github.com/${user.login}"
                        tvGithubLink.text = githubLinkText
                    }
                }
                is Hasil.Error -> {
                    Toast.makeText(this, result.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is Hasil.Loading -> {
                    binding.progressBar.isVisible = result.isLoading
                }
            }
        }

        viewModel.resultFavSuccess.observe(this) {
            binding.btnFav.changeIconColor(R.color.red)
        }

        viewModel.resultFavDelete.observe(this) {
            binding.btnFav.changeIconColor(R.color.white)
        }

        viewModel.findUserFav(item?.id ?: 0) {
            binding.btnFav.changeIconColor(R.color.red)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val item = intent.getParcelableExtra<User>("item")
        val username = item?.login.orEmpty()
        val fragments = mutableListOf(
            DetailUserFragment.newInstance(DetailUserFragment.FOLLOWERS),
            DetailUserFragment.newInstance(DetailUserFragment.FOLLOWING)
        )
        val titles = listOf(
            getString(R.string.followers),
            getString(R.string.following)
        )
        val adapter = DetailUserAdapter(this, fragments)
        binding.viewpager.adapter = adapter

        setupViewModelObservers(username, item)

        setupTabLayoutAndViewPager(titles, username)

        binding.btnFav.setOnClickListener {
            viewModel.setUserFav(item)
        }
    }

    override fun onResume() {
        super.onResume()
        val item = intent.getParcelableExtra<User>("item")
        val username = item?.login.orEmpty()
        viewModel.getUserFollowers(username)
        viewModel.getUserDetails(username)
        viewModel.findUserFav(item?.id ?: 0) {
            binding.btnFav.changeIconColor(R.color.red)
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
