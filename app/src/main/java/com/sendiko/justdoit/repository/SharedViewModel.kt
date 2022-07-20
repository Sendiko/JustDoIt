package com.sendiko.justdoit.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private val _username = MutableLiveData<String>()
    var username : LiveData<String> = _username

    fun saveUsername(username : String){
        _username.value = username
    }

}