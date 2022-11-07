package com.example.tranyapp.screens.meditations

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tranyapp.model.meditations.InMemoryMeditationsRepository
import com.example.tranyapp.model.meditations.entities.Meditation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class MeditationRunnableViewModel(
    title: String,
    inMemoryMeditationsRepository: InMemoryMeditationsRepository
) : ViewModel() {

    private val _meditationRunnable = MutableLiveData<Meditation>()
    val meditationRunnable: LiveData<Meditation> = _meditationRunnable


    init {

        viewModelScope.launch {
            _meditationRunnable.value = inMemoryMeditationsRepository.getMeditation(title)
        }

    }

}