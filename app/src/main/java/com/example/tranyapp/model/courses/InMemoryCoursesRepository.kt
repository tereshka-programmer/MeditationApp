package com.example.tranyapp.model.courses

import androidx.lifecycle.Lifecycle
import com.example.tranyapp.model.courses.entities.Course
import com.example.tranyapp.model.meditations.InMemoryMeditationsRepository
import com.github.javafaker.Faker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class InMemoryCoursesRepository(
    meditationsRepository: InMemoryMeditationsRepository,
    scope: CoroutineScope,
) : CoursesRepository {

    private val faker = Faker.instance()


    private var currentCoursesFlow = MutableStateFlow<List<Course>>(listOf())


    init {

        scope.launch {
            var counter = -1

            meditationsRepository.getAllMeditations().collect { meditations ->
                currentCoursesFlow.value =
                    images.map {
                        counter++
                        Course(
                            title = faker.book().title(),
                            description = faker.book().genre(),
                            image = it,
                            value = meditations.chunked(images.size)[counter]
                        )
                    }
            }
        }

//        var counter = -1
//
//        currentCoursesFlow = meditationsRepository.getAllMeditations().map { meditations ->
//            images.map {
//                counter++
//                Course(
//                    title = faker.book().title(),
//                    description = faker.book().genre(),
//                    image = it,
//                    value = meditations.chunked(images.size)[counter]
//                )
//
//            }
//        } as MutableStateFlow<List<Course>>
    }


    override fun getAllCourses(): Flow<List<Course>> = currentCoursesFlow

    override suspend fun getCourse(title: String): Course {
        return currentCoursesFlow.value.first { it.title == title }
    }

    companion object {
        val images = listOf(
            "https://images.unsplash.com/photo-1609066748900-60c1ab3d8552?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80",
            "https://images.unsplash.com/photo-1495954380655-01609180eda3?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80",
            "https://images.unsplash.com/photo-1418985991508-e47386d96a71?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1470&q=80",
            "https://images.unsplash.com/photo-1582614378336-15327b1f6a0d?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=687&q=80",
            "https://images.unsplash.com/photo-1606041008023-472dfb5e530f?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=688&q=80"
        )
    }
}