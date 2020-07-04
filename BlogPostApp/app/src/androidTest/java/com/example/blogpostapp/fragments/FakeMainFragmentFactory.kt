package com.example.blogpostapp.fragments

import androidx.fragment.app.FragmentFactory
import com.example.blogpostapp.ui.DetailFragment
import com.example.blogpostapp.ui.FinalFragment
import com.example.blogpostapp.ui.ListFragment
import com.example.blogpostapp.ui.UICommunicationListener
import com.example.blogpostapp.util.GlideManager
import com.example.blogpostapp.viewmodels.FakeMainViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Singleton
class FakeMainFragmentFactory
@Inject
constructor(
    private val viewModelFactory: FakeMainViewModelFactory,
    private val requestManager: GlideManager
) : FragmentFactory() {

    // used for setting a mock<UICommunicationListener>
    lateinit var uICommunicationListener: UICommunicationListener

    override fun instantiate(classLoader: ClassLoader, className: String) =

        when (className) {

            ListFragment::class.java.name -> {
                val fragment = ListFragment(viewModelFactory, requestManager)
                if (::uICommunicationListener.isInitialized) {
                    fragment.setUICommunicationListener(uICommunicationListener)
                }
                fragment
            }

            DetailFragment::class.java.name -> {
                val fragment = DetailFragment(viewModelFactory, requestManager)
                if (::uICommunicationListener.isInitialized) {
                    fragment.setUICommunicationListener(uICommunicationListener)
                }
                fragment
            }

            FinalFragment::class.java.name -> {
                val fragment = FinalFragment(viewModelFactory, requestManager)
                if (::uICommunicationListener.isInitialized) {
                    fragment.setUICommunicationListener(uICommunicationListener)
                }
                fragment
            }

            else -> {
                super.instantiate(classLoader, className)
            }
        }
}
