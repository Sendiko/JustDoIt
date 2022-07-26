package com.sendiko.justdoit.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sendiko.justdoit.model.FailedMessage
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

   private lateinit var auth : FirebaseAuth

   private val _isFailed = MutableLiveData<FailedMessage>()
   val isFailed : LiveData<FailedMessage> = _isFailed

   private val _isLoading = MutableLiveData<Boolean>()
   val isLoading : LiveData<Boolean> = _isLoading

   fun loginWithUser(e : String, p : String) : LiveData<Boolean>{
      val isDone = MutableLiveData<Boolean>()
      _isLoading.value = true
      viewModelScope.launch {
         auth = Firebase.auth
         auth.signInWithEmailAndPassword(e, p).addOnCompleteListener {
            when{
               it.isSuccessful -> {
                  isDone.value = true
                  _isLoading.value = true
               }
            }
         }.addOnFailureListener {
            isDone.value = true
            _isLoading.value = false
            _isFailed.value = FailedMessage(true, it.message)
         }
      }
      return isDone
   }

}