package com.example.blogpostapp.ui

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.example.blogpostapp.R
import com.example.blogpostapp.TestBaseApplication
import com.example.blogpostapp.di.TestAppComponent
import com.example.blogpostapp.ui.viewmodel.state.MainStateEvent
import com.example.blogpostapp.util.Constants
import com.example.blogpostapp.util.EspressoIdlingResourceRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@RunWith(AndroidJUnit4ClassRunner::class)
class ListFragmentErrorTest : BaseMainActivityTests() {

    @get:Rule
    val espressoIdlingResourceRule = EspressoIdlingResourceRule()

    @Test
    fun isErrorDialogShown_UnknownError() {
        // setup
        val app = InstrumentationRegistry.getInstrumentation()
            .targetContext.applicationContext as TestBaseApplication

        val apiService = configureFakeApiService(
            blogDataSource = Constants.SERVER_ERROR_FILENAME,
            categoriesDataSource = Constants.CATEGORIES_DATA_FILENAME,
            networkDelay = 0L,
            application = app
        )

        configureFakeRepository(
            apiService = apiService,
            application = app
        )

        injectTest(app)

        val scenario = launchActivity<MainActivity>()

        onView(withText(R.string.text_error))
            .check(matches(isDisplayed()))

        onView(withSubstring(Constants.UNKNOWN_ERROR))
            .check(matches(isDisplayed()))

    }


    @Test
    fun doesNetworkTimeout_NetworkTimeoutError() {
        // setup
        val app = InstrumentationRegistry.getInstrumentation()
            .targetContext.applicationContext as TestBaseApplication

        val apiService = configureFakeApiService(
            blogDataSource = Constants.BLOG_POSTS_DATA_FILENAME,
            categoriesDataSource = Constants.CATEGORIES_DATA_FILENAME,
            networkDelay = 4000L,
            application = app
        )

        configureFakeRepository(
            apiService = apiService,
            application = app
        )

        injectTest(app)

        val scenario = launchActivity<MainActivity>()

        onView(withText(R.string.text_error))
            .check(matches(isDisplayed()))

        onView(withSubstring(Constants.NETWORK_ERROR_TIMEOUT))
            .check(matches(isDisplayed()))

    }

    @Test
    fun isErrorDialogShown_CannotRetrieveCategories() {
        // setup
        val app = InstrumentationRegistry.getInstrumentation()
            .targetContext.applicationContext as TestBaseApplication

        val apiService = configureFakeApiService(
            blogDataSource = Constants.BLOG_POSTS_DATA_FILENAME,
            categoriesDataSource = Constants.SERVER_ERROR_FILENAME,
            networkDelay = 0L,
            application = app
        )

        configureFakeRepository(
            apiService = apiService,
            application = app
        )

        injectTest(app)

        val scenario = launchActivity<MainActivity>()

        onView(withText(R.string.text_error))
            .check(matches(isDisplayed()))

        onView(withSubstring(MainStateEvent.GetCategories().errorInfo()))
            .check(matches(isDisplayed()))

    }

    @Test
    fun isErrorDialogShown_CannotRetrieveBlogPosts() {
        // setup
        val app = InstrumentationRegistry.getInstrumentation()
            .targetContext.applicationContext as TestBaseApplication

        val apiService = configureFakeApiService(
            blogDataSource = Constants.SERVER_ERROR_FILENAME,
            categoriesDataSource = Constants.CATEGORIES_DATA_FILENAME,
            networkDelay = 0L,
            application = app
        )

        configureFakeRepository(
            apiService = apiService,
            application = app
        )

        injectTest(app)

        val scenario = launchActivity<MainActivity>()

        onView(withText(R.string.text_error))
            .check(matches(isDisplayed()))

        onView(withSubstring(MainStateEvent.GetAllBlogs().errorInfo()))
            .check(matches(isDisplayed()))

    }


    override fun injectTest(application: TestBaseApplication) {
        (application.appComponent as TestAppComponent)
            .inject(this)
    }
}