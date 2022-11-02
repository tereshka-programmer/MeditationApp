package com.example.tranyapp.model.courses

import com.example.tranyapp.model.courses.entities.Course
import kotlinx.coroutines.flow.Flow

interface CoursesRepository {

    // Just get all available courses from memory
    fun getAllCourses(): Flow<List<Course>>

    // Get course by name
    suspend fun getCourse(title: String): Course


}