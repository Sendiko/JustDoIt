package com.sendiko.justdoit.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sendiko.justdoit.repository.model.FailedMessage

class LoginViewModel : ViewModel() {

   private val _isFailed = MutableLiveData<FailedMessage>()
   val isFailed : LiveData<FailedMessage> = _isFailed

   private val _isLoading = MutableLiveData<Boolean>()
   val isLoading : LiveData<Boolean> = _isLoading

}