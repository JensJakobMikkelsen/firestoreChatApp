package com.example.firestorechatapp.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firestorechatapp.Message


// not used
// https://developer.android.com/guide/fragments/communicate
class userViewModel : ViewModel() {

    val password =MutableLiveData<Any>()
    val username =MutableLiveData<Any>()
    fun setMsgCommunicator(_password:String, _username: String){
        password.setValue(_password)
        username.setValue(_username)
    }
}
