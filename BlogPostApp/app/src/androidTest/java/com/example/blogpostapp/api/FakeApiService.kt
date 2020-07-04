package com.example.blogpostapp.api

import com.example.blogpostapp.models.BlogPost
import com.example.blogpostapp.models.Category
import com.example.blogpostapp.util.Constants
import com.example.blogpostapp.util.JsonUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeApiService
@Inject
constructor(
    private val jsonUtil: JsonUtil
) : ApiService {

    var blogPostJsonFileName: String = Constants.BLOG_POSTS_DATA_FILENAME
    var categoriesFileName: String = Constants.CATEGORIES_DATA_FILENAME
    var networkDelay: Long = 0L

    override suspend fun getBlogPosts(category: String): List<BlogPost> {
        val rawJson = jsonUtil.readJSONFromAsset(blogPostJsonFileName)
        val blogs = Gson().fromJson<List<BlogPost>>(
            rawJson,
            object : TypeToken<List<BlogPost>>() {}.type
        )
        val filteredBlogPost = blogs.filter { blogPost ->
            blogPost.category == category
        }
        delay(networkDelay)
        return filteredBlogPost

    }

    override suspend fun getAllBlogPosts(): List<BlogPost> {
        val rawJson = jsonUtil.readJSONFromAsset(blogPostJsonFileName)
        val blogs = Gson().fromJson<List<BlogPost>>(
            rawJson,
            object : TypeToken<List<BlogPost>>() {}.type
        )

        delay(networkDelay)
        return blogs
    }

    override suspend fun getCategories(): List<Category> {
        val rawJson = jsonUtil.readJSONFromAsset(categoriesFileName)
        val categories = Gson().fromJson<List<Category>>(
            rawJson,
            object : TypeToken<List<Category>>() {}.type
        )

        delay(networkDelay)
        return categories
    }

}
