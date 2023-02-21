package com.example.tranyapp.screens.player

import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.tranyapp.App
import com.example.tranyapp.R
import com.example.tranyapp.databinding.SongRunnableBinding
import com.example.tranyapp.model.songs.entities.Song
import com.example.tranyapp.utils.MusicState
import com.example.tranyapp.utils.PlayerRemote
import com.example.tranyapp.utils.viewModelCreator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SongRunnableFragment : Fragment(R.layout.song_runnable) {

    private lateinit var binding: SongRunnableBinding
    private val viewModel by viewModelCreator { SongRunnableViewModel(App.songRepository) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SongRunnableBinding.bind(view)

        binding.btPlay.setOnClickListener { viewModel.onPlay() }
        binding.btNext.setOnClickListener { viewModel.onNext() }
        binding.btPrevious.setOnClickListener { viewModel.onPrevious() }

        viewModel.currentSong.observe(viewLifecycleOwner) {
            redrawScreen(it)
            binding.exoProgress.setDuration(it.duration)
        }

        viewModel.playerState.observe(viewLifecycleOwner) {
            playChecker(it.isPlaying)
        }

        lifecycleScope.launchWhenStarted {
            PlayerRemote.playerService?.followCurrentPosition()?.collect { position ->
                withContext(Dispatchers.Main) {
                    binding.exoProgress.setPosition(position)
                    Log.d("CHECKERR", "$position")
                }
            }
        }

        PlayerRemote.playerService?.let { binding.exoProgress.addListener(it) }

    }

    private fun playChecker(isPlaying: Boolean) {
        if (!isPlaying) binding.btPlay.background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_play)
        else binding.btPlay.background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_stop)
    }

    private fun redrawScreen(song: Song) {
        binding.titleOfSong.text = song.name
        lifecycleScope.launch(Dispatchers.Default) {
            val imgByte = getSongThumbnail(song.path)
            withContext(Dispatchers.Main) {
                Glide.with(requireContext()).asBitmap().load(imgByte)
                    .placeholder(R.drawable.album)
                    .error(R.drawable.album)
                    .optionalCircleCrop()
                    .into(binding.songImage)
            }
        }
    }

    private fun getSongThumbnail(songPath: String?): ByteArray? {
        val retriever = MediaMetadataRetriever()
        try {
            if (songPath != null)
                retriever.setDataSource(songPath)
        } catch (e: Exception) {
            Log.e("SongsAdapter", e.message.toString())
        }

        val imgByte = retriever.embeddedPicture
        retriever.release()
        return imgByte
    }

}