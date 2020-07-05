package com.example.blogpostapp.di

import androidx.lifecycle.ViewModelProvider
import com.example.blogpostapp.repository.FakeMainRepositoryImpl
import com.example.blogpostapp.viewmodels.FakeMainViewModelFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Module
object TestViewModelModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideViewModelFactory(
        mainRepository: FakeMainRepositoryImpl
    ): ViewModelProvider.Factory {
        return FakeMainViewModelFactory(
            mainRepository
        )
    }

}