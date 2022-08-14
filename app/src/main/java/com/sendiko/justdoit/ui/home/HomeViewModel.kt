package com.sendiko.justdoit.ui.home

import androidx.lifecycle.*
import com.sendiko.justdoit.repository.preferences.DataPreferences
import kotlinx.coroutines.launch

class HomeViewModel(private val data : DataPreferences) : ViewModel() {

    fun setSortKey(sort : String) = viewModelScope.launch {
        data.setSortKey(sort)
    }

    fun getSortKey() : LiveData<String> {
        return data.getSortKey().asLiveData()
    }

    class HomeViewModelFactory(private val auth : DataPreferences) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            when {
                modelClass.isAssignableFrom(HomeViewModel::class.java) -> return HomeViewModel(auth) as T
                else -> throw IllegalArgumentException("Unkown model class : " + modelClass.name)
            }
        }
    }
}