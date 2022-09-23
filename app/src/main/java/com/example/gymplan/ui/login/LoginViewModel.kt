package com.example.gymplan.ui.login

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val user: FirebaseAuth
) : ViewModel() {
    // TODO: Implement the ViewModel
}