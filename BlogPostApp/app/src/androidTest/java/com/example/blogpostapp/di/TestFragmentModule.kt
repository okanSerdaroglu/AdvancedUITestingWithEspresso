package com.example.blogpostapp.di

import androidx.fragment.app.FragmentFactory
import com.example.blogpostapp.fragments.FakeMainFragmentFactory
import com.example.blogpostapp.util.FakeGlideRequestManager
import com.example.blogpostapp.viewmodels.FakeMainViewModelFactory
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Module
object TestFragmentModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideFragmentFactory(
        viewModelFactory: FakeMainViewModelFactory,
        glideRequestManager: FakeGlideRequestManager
    ): FragmentFactory {
        return FakeMainFragmentFactory(viewModelFactory, glideRequestManager)
    }

}