package com.sendiko.justdoit.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.sendiko.justdoit.repository.model.FailedMessage
import com.sendiko.justdoit.repository.model.Task
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

   private val context by lazy { getApplication<Application>().applicationContext }

   val _isFailed = MutableLiveData<FailedMessage>()
   val isFailed : LiveData<FailedMessage> = _isFailed

   private val _isLoading = MutableLiveData<Boolean>()
   val isLoading : LiveData<Boolean> = _isLoading

   private val _isEmpty = MutableLiveData<Boolean>()
   val isEmpty : LiveData<Boolean> = _isEmpty

}