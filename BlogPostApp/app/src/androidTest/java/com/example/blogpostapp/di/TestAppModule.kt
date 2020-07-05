package com.example.blogpostapp.di

import android.app.Application
import com.example.blogpostapp.util.FakeGlideRequestManager
import com.example.blogpostapp.util.GlideManager
import com.example.blogpostapp.util.JsonUtil
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object TestAppModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideGlideRequestManager(): GlideManager {
        return FakeGlideRequestManager()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideJsonUtil(application: Application): JsonUtil {
        return JsonUtil(application)
    }

}