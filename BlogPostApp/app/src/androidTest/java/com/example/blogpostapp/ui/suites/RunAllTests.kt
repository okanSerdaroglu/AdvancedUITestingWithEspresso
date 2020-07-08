package com.example.blogpostapp.ui.suites

import com.example.blogpostapp.ui.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.runner.RunWith
import org.junit.runners.Suite

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@RunWith(Suite::class)
@Suite.SuiteClasses(
    MainNavigationTest::class,
    ListFragmentNavigationTest::class,
    ListFragmentIntegrationTest::class,
    ListFragmentErrorTest::class,
    DetailFragmentTest::class
)
class RunAllTests