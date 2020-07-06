package com.example.blogpostapp.ui

import com.example.blogpostapp.TestBaseApplication
import com.example.blogpostapp.api.FakeApiService
import com.example.blogpostapp.di.TestAppComponent
import com.example.blogpostapp.repository.FakeMainRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
abstract class BaseMainActivityTests {

    abstract fun injectTest(application: TestBaseApplication)

    fun configureFakeApiService(
        blogDataSource: String? = null,
        categoriesDataSource: String? = null,
        networkDelay: Long? = null,
        application: TestBaseApplication
    ): FakeApiService {
        val apiService = (application.appComponent as TestAppComponent).apiService
        blogDataSource?.let {
        apiService.blogPostJsonFileName = it
        }
        categoriesDataSource?.let {
        apiService.categoriesFileName = it
        }
        networkDelay?.let {
        apiService.networkDelay = it
        }
        return apiService
    }

    fun configureFakeRepository(
        apiService: FakeApiService,
        application: TestBaseApplication
    ): FakeMainRepositoryImpl {
        val mainRepository = (application.appComponent as TestAppComponent).mainRepository
        mainRepository.apiService = apiService
        return mainRepository
    }

}