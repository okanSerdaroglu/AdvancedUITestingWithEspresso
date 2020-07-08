package com.example.blogpostapp.ui

import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.example.blogpostapp.R
import com.example.blogpostapp.TestBaseApplication
import com.example.blogpostapp.di.TestAppComponent
import com.example.blogpostapp.ui.BlogPostListAdapter.*
import com.example.blogpostapp.ui.viewmodel.state.MainViewState
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

    @Test
    fun isCategoryListEmpty() {

        // setup
        val app = InstrumentationRegistry.getInstrumentation()
            .targetContext.applicationContext as TestBaseApplication

        val apiService = configureFakeApiService(
            blogDataSource = Constants.BLOG_POSTS_DATA_FILENAME,
            categoriesDataSource = Constants.EMPTY_LIST,
            networkDelay = 0L,
            application = app
        )

        configureFakeRepository(
            apiService = apiService,
            application = app
        )

        injectTest(app)

        val scenario = launchActivity<MainActivity>().onActivity { mainActivity ->

            val toolbar: Toolbar = mainActivity.findViewById(R.id.tool_bar)

            // wait for the jobs to complete to open menu
            mainActivity.viewModel.viewState.observe(mainActivity, Observer { viewState ->
                if (viewState.activeJobCounter.size == 0) {
                    toolbar.showOverflowMenu()
                }
            })
        }

        // assert
        onView(withSubstring("earthporn"))
            .check(doesNotExist())

        onView(withSubstring("dogs"))
            .check(doesNotExist())

        onView(withSubstring("fun"))
            .check(doesNotExist())

        onView(withSubstring("All"))
            .check(matches(isDisplayed()))

    }

    @Test
    fun checkListData_testScrolling() {
        // setup
        val app = InstrumentationRegistry.getInstrumentation()
            .targetContext.applicationContext as TestBaseApplication

        val apiService = configureFakeApiService(
            blogDataSource = Constants.BLOG_POSTS_DATA_FILENAME,
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

        val recyclerView = onView(withId(R.id.recycler_view))

        recyclerView.check(matches(isDisplayed()))

        recyclerView.perform(
            RecyclerViewActions.scrollToPosition<BlogPostViewHolder>(
                5
            )
        )

        onView(withText("Mountains in Washington")).check(matches(isDisplayed()))

        recyclerView.perform(
            RecyclerViewActions.scrollToPosition<BlogPostViewHolder>(8)
        )

        onView(withText("Blake Posing for his Website")).check(matches(isDisplayed()))

        recyclerView.perform(
            RecyclerViewActions.scrollToPosition<BlogPostViewHolder>(0)
        )

        onView(withText("Vancouver PNE 2019")).check(matches(isDisplayed()))

        onView(withId(R.id.no_data_textview))
            .check(matches(withEffectiveVisibility(Visibility.GONE)))

    }

    @Test
    fun checkListData_onCategoryChange_toEarthPorn() {
        // setup
        val app = InstrumentationRegistry
            .getInstrumentation()
            .targetContext.applicationContext as TestBaseApplication


        val apiService = configureFakeApiService(
            blogDataSource = Constants.BLOG_POSTS_DATA_FILENAME,
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

        scenario.onActivity { mainActivity ->
            val toolbar: Toolbar = mainActivity.findViewById(R.id.tool_bar)

            mainActivity.viewModel.viewState.observe(mainActivity,
                object : Observer<MainViewState> {
                    override fun onChanged(viewState: MainViewState?) {
                        if (viewState?.activeJobCounter?.size == 0) {
                            toolbar.showOverflowMenu()
                            mainActivity.viewModel.viewState.removeObserver(this)
                        }
                    }
                })
        }

        val categoryName = "earthporn"
        onView(withText(categoryName)).perform(click())

        val recyclerView = onView(withId(R.id.recycler_view))
        recyclerView.check(matches(isDisplayed()))


        recyclerView.perform(
            RecyclerViewActions.scrollToPosition<BlogPostViewHolder>(0)
        )
        onView(withText("Mountains in Washington")).check(matches(isDisplayed()))

        recyclerView.perform(
            RecyclerViewActions.scrollToPosition<BlogPostViewHolder>(1)
        )
        onView(withText("France Mountain Range")).check(matches(isDisplayed()))

        onView(withText("Lounging Dogs")).check(doesNotExist())
    }

    @Test
    fun isInstanceStateSaveAndRestored_OnActivityDestroyed() {
        // setup
        val app = InstrumentationRegistry.getInstrumentation()
            .targetContext.applicationContext as TestBaseApplication

        val apiService = configureFakeApiService(
            blogDataSource = Constants.BLOG_POSTS_DATA_FILENAME,
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

        onView(withId(R.id.recycler_view))
            .check(matches(isDisplayed()))

        onView(withId(R.id.recycler_view)).perform(
            RecyclerViewActions.scrollToPosition<BlogPostViewHolder>(8)
        )

        onView(withText("Blake Posing for his Website")).check(matches(isDisplayed()))

        scenario.recreate()

        onView(withText("Blake Posing for his Website")).check(matches(isDisplayed()))
    }


    override fun injectTest(application: TestBaseApplication) {
        (application.appComponent as TestAppComponent)
            .inject(this)
    }
}