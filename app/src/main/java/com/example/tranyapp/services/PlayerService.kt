package com.example.tranyapp.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.media.app.NotificationCompat
import com.example.tranyapp.App
import com.example.tranyapp.R
import com.example.tranyapp.receivers.NotificationActionBroadcastReceiver
import com.example.tranyapp.screens.player.SongRunnableViewModel.*
import com.example.tranyapp.utils.Constants.NOTIFICATION_CHANNEL_ID
import com.example.tranyapp.utils.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.tranyapp.utils.Constants.NOTIFICATION_ID
import com.example.tranyapp.utils.MusicState
import com.example.tranyapp.utils.PlayerHelper.getSongThumbnail
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.TimeBar
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

//Actions for notification action buttons
const val ACTION_PREVIOUS = "action previous"
const val ACTION_PLAY_PAUSE = "action play pause"
const val ACTION_NEXT = "action next"

class PlayerService : Service(), Player.Listener, TimeBar.OnScrubListener {

    private val TAG = "MyPlayerService"

    var musicState = MusicState.STOP
    private var exoPlayer: ExoPlayer? = null
    private var playlist = emptyList<MediaItem>()
    var currentPosition: Int = 0
    private var isPrepared: Boolean = false
    private val scope = CoroutineScope(Dispatchers.IO)

    private val binder by lazy { MusicBinder() }

    val currentSongFlow = MutableStateFlow(0)
    val isPlayingFlow = MutableStateFlow(PlayerState(false))

    override fun onCreate() {
        super.onCreate()
        initializeExoPlayer()
    }

    override fun onBind(intent: Intent): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent != null && intent.action != null) {
            when (intent.action) {
                ACTION_PREVIOUS -> {
                    playPreviousSong()
                    Log.d(TAG, "INTENT WITH PREVIOUS ACTION")
                }
                ACTION_PLAY_PAUSE -> {
                    if (isPlayingFlow.value.isPlaying) {
                        pauseMusic()
                    } else playMusic()
                    Log.d(TAG, "INTENT WITH PLAY PAUSE ACTION")
                }
                ACTION_NEXT -> {
                    playNextSong()
                    Log.d(TAG, "INTENT WITH NEXT ACTION")
                }
                else -> Unit
            }
        }

        return START_STICKY
    }


    fun runAction(state: MusicState) {
        musicState = state
        when (state) {
            MusicState.PLAY -> {
                playMusic()
                scope.launch {
                    isPlayingFlow.emit(PlayerState(true))
                }
                startForegroundService()
            }
            MusicState.PAUSE -> {
                pauseMusic()
                scope.launch {
                    isPlayingFlow.emit(PlayerState(false))
                }
                startForegroundService()
            }
            MusicState.NEXT -> {
                playNextSong()
                startForegroundService()
            }
            MusicState.PREVIOUS -> {
                playPreviousSong()
                startForegroundService()
            }
            MusicState.STOP -> stopMusic()
        }
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        scope.launch {
            currentSongFlow.emit(playlist.indexOf(mediaItem))
        }
    }

    fun playSongFromList(position: Int) {
        if (musicState == MusicState.STOP || musicState == MusicState.PAUSE) exoPlayer?.play()
        exoPlayer?.seekTo(position, 0)
        currentPosition = position
        scope.launch {
            isPlayingFlow.emit(PlayerState(true))
        }
    }

    private fun initializeExoPlayer() {
        if (playlist.isEmpty()) playlist = App.songRepository.getPlaylistForPlayer()
        exoPlayer = ExoPlayer.Builder(this)
            .build()
        preparePlayer()
    }

    private fun preparePlayer() {
        exoPlayer?.addListener(this)
        exoPlayer?.setMediaItems(playlist)
        exoPlayer?.prepare()
        startForegroundService()
    }

    fun followCurrentPosition(): Flow<Long> = flow {
        while (true) {
            exoPlayer?.currentPosition?.let { emit(it) }
            delay(1000)
        }
    }

    private fun playMusic() {
        if (!isPrepared) {
            preparePlayer()
            exoPlayer?.play()
            isPrepared = true
        } else {
            exoPlayer?.play()
        }
    }

    private fun pauseMusic() {
        if (exoPlayer?.isPlaying!!) exoPlayer?.pause()
    }

    private fun playNextSong() {
        if (exoPlayer?.hasNextMediaItem()!!) {
            exoPlayer?.seekToNextMediaItem()
        } else {
            exoPlayer?.seekToDefaultPosition(0)
        }
    }

    private fun playPreviousSong() {
        if (exoPlayer?.hasPreviousMediaItem()!!) {
            exoPlayer?.seekToPreviousMediaItem()
        } else {
            exoPlayer?.seekToDefaultPosition(playlist.lastIndex)
        }
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

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    override fun onScrubStart(timeBar: TimeBar, position: Long) {
    }

    override fun onScrubMove(timeBar: TimeBar, position: Long) {
    }

    override fun onScrubStop(timeBar: TimeBar, position: Long, canceled: Boolean) {
        exoPlayer?.seekTo(position)
    }

    private fun startForegroundService() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val playPauseIntent = Intent(this, NotificationActionBroadcastReceiver::class.java).also {
            it.action = ACTION_PLAY_PAUSE
        }
        val playPausePendingIntent =
            PendingIntent.getBroadcast(this, 0, playPauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val nextSongIntent = Intent(this, NotificationActionBroadcastReceiver::class.java).also {
            it.action = ACTION_NEXT
        }
        val nextSongPendingIntent =
            PendingIntent.getBroadcast(this, 0, nextSongIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val previousSongIntent =
            Intent(this, NotificationActionBroadcastReceiver::class.java).also {
                it.action = ACTION_PREVIOUS
            }
        val previousSongPendingIntent = PendingIntent.getBroadcast(this,
            0,
            previousSongIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        val currentSong = App.songRepository.getSongByPosition(currentSongFlow.value)

        val imgByte = getSongThumbnail(currentSong.path)
        val bitmap = if (imgByte != null)
            BitmapFactory.decodeByteArray(imgByte, 0, imgByte.size)
        else
            BitmapFactory.decodeResource(this.resources, R.drawable.album)

        var playPauseDrawable = R.drawable.ic_play
        if (exoPlayer != null) {
            playPauseDrawable = if (isPlayingFlow.value.isPlaying) {
                R.drawable.ic_stop
            } else {
                R.drawable.ic_play
            }
        }

        val builder = androidx.core.app.NotificationCompat
            .Builder(this, NOTIFICATION_CHANNEL_ID).setOngoing(true).apply {
                priority = androidx.core.app.NotificationCompat.PRIORITY_MAX
                setCategory(androidx.core.app.NotificationCompat.CATEGORY_SERVICE)
                setVisibility(androidx.core.app.NotificationCompat.VISIBILITY_PUBLIC)
                setContentTitle(currentSong.name)
                setContentText(currentSong.artistName)
                setOngoing(isPlayingFlow.value.isPlaying)
                addAction(R.drawable.ic_skip_previous, "Previous", previousSongPendingIntent)
                addAction(playPauseDrawable, "Play", playPausePendingIntent)
                addAction(R.drawable.ic_navigate_next, "Next", nextSongPendingIntent)
                setLargeIcon(bitmap)
                setSmallIcon(R.drawable.album)
                setShowWhen(false)
                setStyle(
                    NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2)
                )
            }

        startForeground(NOTIFICATION_ID, builder.build())
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        channel.description = "The playing notification provides actions for play/pause etc."
        channel.enableLights(false)
        channel.enableVibration(false)
        channel.setShowBadge(false)
        notificationManager.createNotificationChannel(channel)
    }

    fun checkOnPlay(): Boolean = isPlayingFlow.value.isPlaying
}