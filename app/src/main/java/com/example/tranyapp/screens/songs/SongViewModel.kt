package com.example.tranyapp.screens.songs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tranyapp.model.songs.InMemorySongRepository
import com.example.tranyapp.model.songs.entities.Song

class SongViewModel(
    songRepository: InMemorySongRepository
) : ViewModel() {

    private val _songs = MutableLiveData<List<Song>>()
    val songs: LiveData<List<Song>> = _songs

    init {
        _songs.value = songRepository.getSongs()
    }



}