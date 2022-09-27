package com.example.gymplan.ui.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import com.example.gymplan.R
import com.example.gymplan.data.repository.UserRepository
import com.example.gymplan.ui.state.AuthState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val user: FirebaseAuth
) : ViewModel() {

    //email and password for the input
    var email: String? = null
    var password: String? = null

//
//    private val disposables = CompositeDisposable()

    fun signIn(context: Context?) = viewModelScope.launch{
        user.signInWithEmailAndPassword(email!!, password!!).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(context, "User logged in", Toast.LENGTH_SHORT).show()
                user.currentUser?.reload()
            } else {
                Toast.makeText(context, task.exception!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun signUp(context: Context?){
        user.createUserWithEmailAndPassword(email!!, password!!).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(context, "User logged in", Toast.LENGTH_SHORT).show()
                user.currentUser?.reload()
            } else {
                Toast.makeText(context, task.exception!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}