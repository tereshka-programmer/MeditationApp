package com.example.tranyapp

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.example.tranyapp.model.courses.InMemoryCoursesRepository
import com.example.tranyapp.model.meditations.InMemoryMeditationsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object App {


    val scope = CoroutineScope(Dispatchers.IO)

    val meditationsRepository = InMemoryMeditationsRepository()
    val coursesRepository = InMemoryCoursesRepository(meditationsRepository, scope)


}