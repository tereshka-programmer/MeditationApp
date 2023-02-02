package com.example.tranyapp.model.songs.entities

data class Song(
    val id: String,
    val name: String,
    val path: String,
    val artistName: String?,
    val albumName: String?,
    val duration: Long
)