package com.example.blogpostapp.repository

import com.example.blogpostapp.util.StateEvent
import com.example.blogpostapp.ui.viewmodel.state.MainViewState
import com.example.blogpostapp.util.DataState
import kotlinx.coroutines.flow.Flow

interface MainRepository{

    fun getBlogs(stateEvent: StateEvent, category: String): Flow<DataState<MainViewState>>

    fun getAllBlogs(stateEvent: StateEvent): Flow<DataState<MainViewState>>

    fun getCategories(stateEvent: StateEvent): Flow<DataState<MainViewState>>
}