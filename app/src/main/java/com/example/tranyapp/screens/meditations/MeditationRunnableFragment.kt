package com.example.tranyapp.screens.meditations

import android.media.AsyncPlayer
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.tranyapp.App
import com.example.tranyapp.R
import com.example.tranyapp.databinding.FragmentMeditationRunnableBinding
import com.example.tranyapp.utils.findTopNavController
import com.example.tranyapp.utils.viewModelCreator
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlin.Exception

class MeditationRunnableFragment : Fragment(R.layout.fragment_meditation_runnable) {

    private lateinit var binding: FragmentMeditationRunnableBinding

    private val viewModel by viewModelCreator { MeditationRunnableViewModel(getTitleFromArgs(), App.meditationsRepository) }

    private val args: MeditationRunnableFragmentArgs by navArgs()

    private var mediaPlayer: MediaPlayer? = null

    private var btFlag = true

    private val listenerOfMediaPlayer = flow<Int> {
        while (true){
            delay(1000)
            try {
                emit(mediaPlayer?.currentPosition ?: 0)
            } catch (e: Exception) {
                print(e)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMeditationRunnableBinding.bind(view)


        viewModel.meditationRunnable.observe(viewLifecycleOwner) {
            binding.titleOfMeditation.text = it.title
            Glide.with(binding.imageMeditation)
                .load(it.image)
                .circleCrop()
                .placeholder(R.drawable.ic_courses)
                .error(R.drawable.ic_courses)
                .into(binding.imageMeditation)

            lifecycleScope.launch {
                prepareMediaPlayer(it.value)
            }
        }

        lifecycleScope.launch {
            listenerOfMediaPlayer.collect {
                withContext(Dispatchers.Main) {
                    binding.seekBar.progress = it
                }
            }
        }

        binding.btPlay.setOnClickListener {
            tapOnPlay()
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, pos: Int, changed: Boolean) {
                if (changed){
                    mediaPlayer?.seekTo(pos)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.apply {
            stop()
            release()
        }
    }

    private suspend fun prepareMediaPlayer(url: String) {
        withContext(Dispatchers.IO) {
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                try {
                    setDataSource(url)
                } catch (e: Exception) {
                    print(e)
                    findTopNavController().navigateUp()
                }
                isLooping = true
                try {
                    prepare()
                } catch (e: Exception) {
                    print(e)
                    withContext(Dispatchers.Main){
                        findNavController().popBackStack()
                    }
                }

            }
        }
        binding.seekBar.max = mediaPlayer!!.duration
        binding.progressBar.visibility = View.GONE
        binding.btPlay.visibility = View.VISIBLE
    }

    private fun getTitleFromArgs(): String = args.title

    private fun tapOnPlay() {
        if (btFlag) {
            binding.btPlay.background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_stop)
            btFlag = false
            mediaPlayer?.start()
        } else {
            binding.btPlay.background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_play)
            btFlag = true
            mediaPlayer?.pause()
        }
    }



}