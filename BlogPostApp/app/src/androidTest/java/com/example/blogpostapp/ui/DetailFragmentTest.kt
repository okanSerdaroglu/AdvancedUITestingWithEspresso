package com.example.blogpostapp.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.example.blogpostapp.R
import com.example.blogpostapp.TestBaseApplication
import com.example.blogpostapp.api.FakeApiService
import com.example.blogpostapp.di.TestAppComponent
import com.example.blogpostapp.fragments.FakeMainFragmentFactory
import com.example.blogpostapp.models.BlogPost
import com.example.blogpostapp.repository.FakeMainRepositoryImpl
import com.example.blogpostapp.ui.viewmodel.setSelectedBlogPost
import com.example.blogpostapp.util.Constants.BLOG_POSTS_DATA_FILENAME
import com.example.blogpostapp.util.Constants.CATEGORIES_DATA_FILENAME
import com.example.blogpostapp.util.FakeGlideRequestManager
import com.example.blogpostapp.util.JsonUtil
import com.example.blogpostapp.viewmodels.FakeMainViewModelFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4ClassRunner::class)
class DetailFragmentTest {

    @Inject
    lateinit var viewModelFactory: FakeMainViewModelFactory

    @Inject
    lateinit var requestManager: FakeGlideRequestManager

    @Inject
    lateinit var jsonUtil: JsonUtil

    @Inject
    lateinit var fragmentFactory: FakeMainFragmentFactory

    private val uiCommunicationListener = mockk<UICommunicationListener>()

    @Before
    fun init() {
        every { uiCommunicationListener.showStatusBar() } just runs
        every { uiCommunicationListener.expandAppBar() } just runs
        every { uiCommunicationListener.hideCategoriesMenu() } just runs
    }

    @Test
    fun isSelectedBlogPostDetailsSet() {

        val app = InstrumentationRegistry
            .getInstrumentation()
            .targetContext
            .applicationContext as TestBaseApplication

        val apiService = configureFakeApiService(
            blogDataSource = BLOG_POSTS_DATA_FILENAME,
            categoriesDataSource = CATEGORIES_DATA_FILENAME,
            networkDelay = 0L,
            application = app
        )

        configureFakeRepository(
            apiService = apiService,
            application = app
        )

        injectTest(app)

        fragmentFactory.uICommunicationListener = uiCommunicationListener

        // run test
        val scenario = launchFragmentInContainer<DetailFragment>(
            factory = fragmentFactory
        )

        val rawJson = jsonUtil.readJSONFromAsset(BLOG_POSTS_DATA_FILENAME)
        val blogs = Gson().fromJson<List<BlogPost>>(
            rawJson,
            object : TypeToken<List<BlogPost>>() {}.type
        )

        val selectedBlogPost = blogs[0]

        scenario.onFragment { fragment ->
            fragment.viewModel.setSelectedBlogPost(selectedBlogPost)
        }

        onView(withId(R.id.blog_title))
            .check(matches(withText(selectedBlogPost.title)))

        onView(withId(R.id.blog_category))
            .check(matches(withText(selectedBlogPost.category)))

        onView(withId(R.id.blog_body))
            .check(matches(withText(selectedBlogPost.body)))

    }

    private fun configureFakeApiService(
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

    private fun injectTest(application: TestBaseApplication) {
        (application.appComponent as TestAppComponent)
            .inject(this)
    }

    private fun configureFakeRepository(
        apiService: FakeApiService,
        application: TestBaseApplication
    ): FakeMainRepositoryImpl {
        val mainRepository = (application.appComponent as TestAppComponent).mainRepository
        mainRepository.apiService = apiService
        return mainRepository
    }

}