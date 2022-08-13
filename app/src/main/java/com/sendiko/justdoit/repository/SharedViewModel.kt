package com.sendiko.justdoit.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

   private val _username = MutableLiveData<String?>()
   var username : MutableLiveData<String?> = _username

   fun saveUsername(username : String){
      _username.value = username
   }

   fun removeUsername(){
      _username.value = null
   }

}