package com.example.blogpostapp.di

import android.app.Application
import com.bumptech.glide.Glide
import com.example.blogpostapp.util.GlideManager
import com.example.blogpostapp.util.GlideRequestManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/* Alternative for Test: 'TestAppModule' */
@Module
object AppModule{

    @JvmStatic
    @Singleton
    @Provides
    fun provideGlideRequestManager(
        application: Application
    ): GlideManager {
        return GlideRequestManager(
            Glide.with(application)
        )
    }



}








