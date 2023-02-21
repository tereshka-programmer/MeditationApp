package com.example.tranyapp.screens.songs

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tranyapp.App
import com.example.tranyapp.R
import com.example.tranyapp.databinding.FragmentSongsBinding
import com.example.tranyapp.model.songs.InMemorySongRepository
import com.example.tranyapp.services.ACTION_NEXT
import com.example.tranyapp.services.PlayerService
import com.example.tranyapp.utils.PlayerRemote
import com.example.tranyapp.utils.viewModelCreator
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class SongFragment : Fragment(R.layout.fragment_songs) {

    private lateinit var binding: FragmentSongsBinding
    private lateinit var adapter: SongAdapter

    private val viewModel by viewModelCreator { SongViewModel(App.songRepository) }

    private val listener: PlayerListener = object : PlayerListener {
        override fun onSongClick(position: Int) {
            PlayerRemote.playerService?.playSongFromList(position)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSongsBinding.bind(view)
        initLayoutManager()
        initAdapter()

        viewModel.songs.observe(viewLifecycleOwner) {
            adapter.updateSongList(it)
        }
    }


    private fun initAdapter() {
        adapter = SongAdapter(requireContext(), mutableListOf(), listener)
        binding.songsRecyclerView.adapter = adapter
    }

    private fun initLayoutManager() {
        binding.songsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }


}