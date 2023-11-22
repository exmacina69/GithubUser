package com.dicoding.githubusers1.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.githubusers1.data.preference.PreferenceSettings
import kotlinx.coroutines.launch

class SettingsViewModel(private val pref: PreferenceSettings) : ViewModel() {

    class Factory(private val pref: PreferenceSettings) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = SettingsViewModel(pref) as T
    }

    fun getTema() = pref.getSettingsTheme().asLiveData()

    fun simpanTema(isDark: Boolean) {
        viewModelScope.launch {
            pref.saveSettingsTheme(isDark)
        }
    }
}