package com.sendiko.justdoit.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class RegisterViewModel : ViewModel() {

   private lateinit var auth : FirebaseAuth

   private val _isFailed = MutableLiveData<Boolean>()
   val isFailed : LiveData<Boolean> = _isFailed

   private val _isLoading = MutableLiveData<Boolean>()
   val isLoading : LiveData<Boolean> = _isLoading

   fun register(){

   }

}