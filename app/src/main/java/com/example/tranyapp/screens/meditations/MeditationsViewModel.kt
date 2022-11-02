package com.example.tranyapp.screens.meditations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tranyapp.model.meditations.InMemoryMeditationsRepository
import com.example.tranyapp.model.meditations.entities.Meditation
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MeditationsViewModel(
    inMemoryMeditationsRepository: InMemoryMeditationsRepository
) : ViewModel() {

    private val _meditations = MutableLiveData<List<Meditation>>()
    val meditation: LiveData<List<Meditation>> = _meditations

    init {

        viewModelScope.launch {
            inMemoryMeditationsRepository.getAllMeditations().collect {
                _meditations.value = it
            }
        }

    }

}