package com.example.blogpostapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.blogpostapp.repository.FakeMainRepositoryImpl
import com.example.blogpostapp.ui.viewmodel.MainViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Singleton

@InternalCoroutinesApi
@Singleton
class FakeMainViewModelFactory
@Inject
constructor(
    private val mainRepository: FakeMainRepositoryImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(mainRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}