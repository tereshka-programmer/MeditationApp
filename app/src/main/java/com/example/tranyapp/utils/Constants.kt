package com.example.tranyapp.utils

import android.provider.MediaStore

object Constants {


    val baseProjection = arrayOf(
        MediaStore.Audio.AudioColumns.TITLE,
        MediaStore.Audio.AudioColumns._ID,
        MediaStore.Audio.AudioColumns.DATA,
        MediaStore.Audio.ArtistColumns.ARTIST,
        MediaStore.Audio.AlbumColumns.ALBUM,
        MediaStore.Audio.AudioColumns.DURATION
    )
}