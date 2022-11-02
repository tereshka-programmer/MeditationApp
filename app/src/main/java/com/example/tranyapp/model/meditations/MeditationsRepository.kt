package com.example.tranyapp.model.meditations

import com.example.tranyapp.model.meditations.entities.Meditation
import kotlinx.coroutines.flow.Flow

interface MeditationsRepository {

    // Just get all available Meditations
    fun getAllMeditations(): Flow<List<Meditation>>

    // Get meditation by title
    suspend fun getMeditation(title: String): Meditation


}