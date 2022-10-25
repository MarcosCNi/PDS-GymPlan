package com.example.gymplan.di

import android.content.Context
import androidx.room.Room
import com.example.gymplan.data.local.ExerciseDatabase
import com.example.gymplan.utils.Constants.DATABASE_NAME
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Singleton
    @Provides
    fun provideExerciseDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        ExerciseDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideExerciseDao(database:ExerciseDatabase) = database.exerciseDao()

    @Singleton
    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun providesFirebaseDatabase() = FirebaseDatabase.getInstance()
}