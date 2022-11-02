package com.example.tranyapp.model.courses.entities

import com.example.tranyapp.model.meditations.entities.Meditation

data class Course(
    val title: String,
    val description: String,
    val image: String,
    val value: List<Meditation>
)
