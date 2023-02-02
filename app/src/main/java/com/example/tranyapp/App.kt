package com.example.tranyapp


import android.content.Context
import com.example.tranyapp.model.courses.InMemoryCoursesRepository
import com.example.tranyapp.model.meditations.InMemoryMeditationsRepository
import com.example.tranyapp.model.songs.InMemorySongRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object App {

    val scope = CoroutineScope(Dispatchers.IO)

    val meditationsRepository = InMemoryMeditationsRepository()
    val coursesRepository = InMemoryCoursesRepository(meditationsRepository, scope)
    val songRepository = InMemorySongRepository()


}