package com.example.gymplan.utils

import android.content.Context
import android.content.Intent
import android.view.View
import com.example.gymplan.ui.home.HomeFragment
import com.example.gymplan.ui.login.SignInFragment


fun Context.startHomeActivity() =
    Intent(this, HomeFragment::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

fun Context.startLoginActivity() =
    Intent(this, SignInFragment::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

fun View.show(){
    visibility = View.VISIBLE
}

fun View.hide(){
    visibility = View.INVISIBLE
}

fun View.gone(){
    visibility = View.GONE
}