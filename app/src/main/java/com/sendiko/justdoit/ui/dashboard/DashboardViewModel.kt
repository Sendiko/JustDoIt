package com.sendiko.justdoit.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sendiko.justdoit.repository.model.FailedMessage

class DashboardViewModel : ViewModel() {

   private val _isFailed = MutableLiveData<FailedMessage>()
   val isFailed : LiveData<FailedMessage> = _isFailed

   private val _isLoading = MutableLiveData<Boolean>()
   val isLoading : LiveData<Boolean> = _isLoading

   private val _isEmpty = MutableLiveData<Boolean>()
   val isEmpty : LiveData<Boolean> = _isEmpty

}