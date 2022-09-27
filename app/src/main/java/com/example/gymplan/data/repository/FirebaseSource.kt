package com.example.gymplan.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Completable
import javax.inject.Inject

class FirebaseSource @Inject constructor(
    private val user: FirebaseAuth
){

    fun login(email: String, password: String) = Completable.create { emitter ->
        user.signInWithEmailAndPassword(email, password).addOnCompleteListener{
            if(!emitter.isDisposed){
                if(it.isSuccessful)
                    emitter.onComplete()
                else
                    emitter.onError(it.exception!!)
            }
        }
    }

    fun register(email: String, password: String) = Completable.create { emitter ->
        Log.e("FirebaseSource", email!!)
        user.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if(!emitter.isDisposed){
                if(it.isSuccessful)
                    emitter.onComplete()
                else
                    emitter.onError(it.exception!!)
            }
        }
    }

    fun logout() = user.signOut()

    fun currentUser() = user.currentUser

}