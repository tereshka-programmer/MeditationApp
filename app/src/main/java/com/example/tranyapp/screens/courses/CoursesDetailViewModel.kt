package com.example.tranyapp.screens.courses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tranyapp.model.courses.InMemoryCoursesRepository
import com.example.tranyapp.model.courses.entities.Course
import com.example.tranyapp.model.meditations.entities.Meditation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CoursesDetailViewModel(
    title: String,
    inMemoryCoursesRepository: InMemoryCoursesRepository
) : ViewModel() {

    private val _course = MutableLiveData<Course>()
    val course: LiveData<Course> = _course

    init {
        viewModelScope.launch {
           _course.value = inMemoryCoursesRepository.getCourse(title)
        }
    }



}