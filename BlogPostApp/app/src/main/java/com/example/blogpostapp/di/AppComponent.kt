package com.example.blogpostapp.di

import android.app.Application
import com.example.blogpostapp.fragments.MainNavHostFragment
import com.example.blogpostapp.ui.MainActivity
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Singleton


@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Singleton
@Component(modules = [
    FragmentModule::class,
    ViewModelModule::class,
    InternalBindingsModule::class,
    AppModule::class,
    RepositoryModule::class
])
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: Application): Builder

        fun build(): AppComponent
    }

    fun inject(mainActivity: MainActivity)

    fun inject(mainNavHostFragment: MainNavHostFragment)

}













