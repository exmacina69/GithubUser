package com.dicoding.githubusers1.ui.setting

import androidx.appcompat.app.AppCompatDelegate
import com.dicoding.githubusers1.data.preference.PreferenceSettings
import com.dicoding.githubusers1.databinding.ActivitySettingBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.dicoding.githubusers1.R
import android.widget.ImageView
import android.widget.TextView

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    private lateinit var imageProfile: ImageView
    private lateinit var textName: TextView
    private lateinit var textGithubLink: TextView
    private val viewModel by viewModels<SettingsViewModel> {
        SettingsViewModel.Factory(PreferenceSettings(this))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()

        viewModel.getTema().observe(this) { isDarkMode ->
            val themeText = if (isDarkMode) getString(R.string.dark_mode) else getString(R.string.light_mode)
            val nightMode = if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO

            binding.smbtnThemeMode.text = themeText
            AppCompatDelegate.setDefaultNightMode(nightMode)
            binding.smbtnThemeMode.isChecked = isDarkMode
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        imageProfile = findViewById(R.id.imageProfile)
        textName = findViewById(R.id.textName)
        textGithubLink = findViewById(R.id.textGithubLink)

        binding.smbtnThemeMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.simpanTema(isChecked)
        }
    }
}
