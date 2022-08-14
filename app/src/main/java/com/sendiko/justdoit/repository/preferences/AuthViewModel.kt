package com.sendiko.justdoit.repository.preferences

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel(private val auth : AuthPreferences) : ViewModel() {

   fun clearUser(){
      viewModelScope.launch {
         auth.clearUsername()
      }
   }

   fun getUser() : LiveData<String> {
      return auth.getUsername().asLiveData()
   }

   fun saveUsername(username : String){
      viewModelScope.launch {
         auth.saveUsername(username)
      }
   }

   fun getLoginState() : LiveData<Boolean> {
      return auth.getLoginState().asLiveData()
   }

   fun setLoginState(isLoggedIn : Boolean){
      viewModelScope.launch(Dispatchers.IO) {
         auth.setLoginState(isLoggedIn)
      }
   }

}

class AuthViewModelFactory(private val auth : AuthPreferences) : ViewModelProvider.NewInstanceFactory(){
   override fun <T : ViewModel> create(modelClass: Class<T>): T {
      when{
         modelClass.isAssignableFrom(AuthViewModel::class.java) -> return AuthViewModel(auth) as T
         else -> throw IllegalArgumentException("Unkown model class : " + modelClass.name)
      }
   }
}
