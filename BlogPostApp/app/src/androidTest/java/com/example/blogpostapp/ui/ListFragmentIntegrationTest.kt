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
class ListFragmentIntegrationTest : BaseMainActivityTests() {

    @get:Rule
    val espressoIdlingResource = EspressoIdlingResourceRule()

    @Test
    fun isBlogListEmpty() {

        // setup
        val app = InstrumentationRegistry.getInstrumentation()
            .targetContext.applicationContext as TestBaseApplication

        val apiService = configureFakeApiService(
            blogDataSource = Constants.EMPTY_LIST,
            categoriesDataSource = Constants.CATEGORIES_DATA_FILENAME,
            networkDelay = 0L,
            application = app
        )

        configureFakeRepository(
            apiService = apiService,
            application = app
        )

        injectTest(app)

        // run test
        val scenario = launchActivity<MainActivity>()

        val recyclerView = onView(withId(R.id.recycler_view))

        recyclerView.check(matches(isDisplayed()))

        onView(withId(R.id.no_data_textview))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))


    }

    override fun injectTest(application: TestBaseApplication) {
        (application.appComponent as TestAppComponent)
            .inject(this)
    }
}