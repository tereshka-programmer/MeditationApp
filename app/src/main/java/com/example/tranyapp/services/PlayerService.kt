package com.example.tranyapp.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.tranyapp.App
import com.example.tranyapp.model.songs.entities.Song
import com.example.tranyapp.utils.MusicState
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player

class PlayerService : Service() {

    private val TAG = "MyPlayerService"

    var musicState = MusicState.STOP
    private var exoPlayer: ExoPlayer? = null
    private var playlist = emptyList<MediaItem>()

    private val binder by lazy { MusicBinder() }

    init {
        Log.d(TAG, "PlayerService has just initialized")
    }

    override fun onCreate() {
        super.onCreate()
        initializeExoPlayer()
    }


    override fun onBind(intent: Intent): IBinder = binder

//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//
//        return START_NOT_STICKY
//    }

    fun runAction(state: MusicState) {
        musicState = state
        when (state) {
            MusicState.PLAY -> playMusic()
            MusicState.PAUSE -> pauseMusic()
            MusicState.NEXT -> playNextSong()
            MusicState.PREVIOUS -> playPreviousSong()
            MusicState.STOP -> stopMusic()
        }
    }

    fun playSongFromList(position: Int) {
        exoPlayer?.seekTo(position, 0)
    }

    private fun initializeExoPlayer() {
        if (playlist.isEmpty()) playlist = App.songRepository.getPlaylistForPlayer()
        exoPlayer = ExoPlayer.Builder(this).build()
        preparePlayer()
    }

    private fun preparePlayer() {
        exoPlayer?.setMediaItems(playlist)
        exoPlayer?.prepare()
    }

    private fun playMusic() {
        preparePlayer()
        exoPlayer?.play()
    }

    private fun pauseMusic() {
        if (exoPlayer?.isPlaying!!) exoPlayer?.pause()
    }

    private fun playNextSong() {
        if (exoPlayer?.hasNextMediaItem()!!) exoPlayer?.seekToNextMediaItem()
        else exoPlayer?.seekToDefaultPosition(0)
    }

    private fun playPreviousSong() {
        if (exoPlayer?.hasPreviousMediaItem()!!) exoPlayer?.seekToPreviousMediaItem()
        else exoPlayer?.seekToDefaultPosition(playlist.lastIndex)
    }

    private fun stopMusic() {
        exoPlayer?.run {
            stop()
            release()
        }
    }

    inner class MusicBinder : Binder() {
        fun getService(): PlayerService = this@PlayerService
    }

}