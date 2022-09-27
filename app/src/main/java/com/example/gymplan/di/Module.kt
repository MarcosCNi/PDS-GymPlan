package com.example.gymplan.di

import com.example.gymplan.data.repository.FirebaseSource
import com.example.gymplan.data.repository.UserRepository
import com.example.gymplan.ui.state.AuthState
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.annotation.Signed
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Singleton
    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

//    @Singleton
//    @Provides
//    fun provideFirebaseSource(user: FirebaseAuth) = FirebaseSource(user)
//
//    @Singleton
//    @Provides
//    fun provideUserRepository(firebase: FirebaseSource) = UserRepository(firebase)
}