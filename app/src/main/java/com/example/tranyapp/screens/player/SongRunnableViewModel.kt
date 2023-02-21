package com.example.tranyapp.screens.player

import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tranyapp.R
import com.example.tranyapp.model.songs.InMemorySongRepository
import com.example.tranyapp.model.songs.entities.Song
import com.example.tranyapp.utils.MusicState
import com.example.tranyapp.utils.PlayerRemote
import kotlinx.coroutines.launch

class SongRunnableViewModel(
    private val inMemorySongRepository: InMemorySongRepository
) : ViewModel() {

    private val _currentSong = MutableLiveData<Song>()
    val currentSong: LiveData<Song> = _currentSong

    private val _playerState = MutableLiveData<PlayerState>(PlayerState(false))
    val playerState: LiveData<PlayerState> = _playerState

    private val _currentPositionOfSong = MutableLiveData<Long>()
    val currentPositionOfSong: LiveData<Long> = _currentPositionOfSong

    init {
        viewModelScope.launch {
            PlayerRemote.playerService?.isPlayingFlow?.collect {
                _playerState.value = it
                Log.d("CHECKER", "$it")
            }
        }
        viewModelScope.launch {
            PlayerRemote.playerService?.currentSongFlow?.collect {
                _currentSong.value = inMemorySongRepository.getSongByPosition(it)
                Log.d("CHECKER", "$it")
            }
        }
    }

    fun onPlay() {
        if (_playerState.value?.isPlaying == false) {
            PlayerRemote.playerService?.runAction(MusicState.PLAY)
        }
        else {
            PlayerRemote.playerService?.runAction(MusicState.PAUSE)
        }
    }

    fun onNext() {
        checkAndPlay()
        PlayerRemote.playerService?.runAction(MusicState.NEXT)
    }

    fun onPrevious() {
        checkAndPlay()
        PlayerRemote.playerService?.runAction(MusicState.PREVIOUS)
    }

    private fun checkAndPlay() {
        if (_playerState.value?.isPlaying == false) onPlay()
    }


    data class PlayerState(
        var isPlaying: Boolean = false
    )
}