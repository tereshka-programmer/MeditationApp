package com.example.tranyapp.screens.courses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tranyapp.model.courses.InMemoryCoursesRepository
import com.example.tranyapp.model.courses.entities.Course
import kotlinx.coroutines.launch

class CoursesViewModel(
    inMemoryCoursesRepository: InMemoryCoursesRepository
) : ViewModel() {

    private val _courses = MutableLiveData<List<Course>>()
    val course: LiveData<List<Course>> = _courses

    init {

        viewModelScope.launch {
            inMemoryCoursesRepository.getAllCourses().collect {
                _courses.value = it
            }
        }

    }

}