package com.example.gymplan.ui.login

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : ViewModel() {

    private val _user = MutableLiveData<FirebaseUser>()
    val user: LiveData<FirebaseUser> get() = _user

    //email and password for the input
    var email: String? = null
    var password: String? = null

//    private val disposables = CompositeDisposable()

    fun signIn(context: Context?) = viewModelScope.launch{
        firebaseAuth.signInWithEmailAndPassword(email!!, password!!).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(context, "User logged in", Toast.LENGTH_SHORT).show()
                _user.value = firebaseAuth.currentUser
            } else {
                Toast.makeText(context, task.exception!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun signUp(context: Context?) = viewModelScope.launch{
        firebaseAuth.createUserWithEmailAndPassword(email!!, password!!).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(context, "User registered", Toast.LENGTH_SHORT).show()
                firebaseAuth.currentUser?.reload()
                _user.value = firebaseAuth.currentUser
            } else {
                Toast.makeText(context, task.exception!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun resetPassword(context: Context?) = viewModelScope.launch {
        firebaseAuth.sendPasswordResetEmail(email!!)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Check your email to reset your password", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
    }
}