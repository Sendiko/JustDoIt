package com.sendiko.justdoit.ui.settings

import androidx.lifecycle.*
import com.sendiko.justdoit.repository.preferences.SettingsPreference
import kotlinx.coroutines.launch

class SettingsViewModel(private val settings: SettingsPreference) : ViewModel() {

    fun setSortListKey(sortKey: String) {
        viewModelScope.launch {
            settings.setSortListKey(sortKey)
        }
    }

    fun getSortListKey(): LiveData<String> {
        return settings.getSortListKey().asLiveData()
    }

    fun setDarkTheme(darkTheme: Boolean) {
        viewModelScope.launch {
            settings.setDarkTheme(darkTheme)
        }
    }

    fun getDarkTheme(): LiveData<Boolean> {
        return settings.getDarkTheme().asLiveData()
    }

    fun setLanguage(language: String) {
        viewModelScope.launch {
            settings.setLanguageKey(language)
        }
    }

    fun getLanguage(): LiveData<String> {
        return settings.getLanguageKey().asLiveData()
    }

    class SettingsViewModelFactory(private val settings: SettingsPreference) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            when {
                modelClass.isAssignableFrom(SettingsViewModel::class.java) -> return SettingsViewModel(
                    settings
                ) as T
                else -> throw IllegalArgumentException("Unknown model class : " + modelClass.name)
            }
        }
    }

}