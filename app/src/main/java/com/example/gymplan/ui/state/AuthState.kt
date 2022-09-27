package com.example.gymplan.ui.state
interface AuthState{
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}
