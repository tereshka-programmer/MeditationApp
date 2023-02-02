package com.example.tranyapp.model.songs

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.tranyapp.model.songs.entities.Song
import com.example.tranyapp.utils.Constants
import com.example.tranyapp.utils.Constants.baseProjection
import com.google.android.exoplayer2.MediaItem

class InMemorySongRepository() {

    private var listOfSongs: List<Song> = listOf()

    fun getPlaylistForPlayer(): List<MediaItem> {
        return listOfSongs.map {
            MediaItem.fromUri(it.path)
        }
    }

    fun initializeSongRepository(context: Context) {
        listOfSongs = getAllSongs(context)
    }

    fun getSongs(): List<Song> = listOfSongs

    private fun getAllSongs(context: Context): List<Song> {
        return songs(makeSongCursor(context))
    }

    private fun songs(cursor: Cursor?): List<Song> {
        val songs = arrayListOf<Song>()
        if (cursor != null && cursor.moveToFirst()) {
            do {
                songs.add(getSongFromCursorImpl(cursor))
            } while (cursor.moveToNext())
        }
        cursor?.close()
        Log.d("SONGTAG", "$songs.s")
        return songs
    }

    private fun getSongFromCursorImpl(cursor: Cursor): Song {
        val title = cursor.getString(0)
        val id = cursor.getString(1)
        val path = cursor.getString(2)
        val artistName = cursor.getString(3)
        val albumName = cursor.getString(4)
        val duration = cursor.getLong(5)
        return Song(id, title, path, artistName, albumName, duration)
    }

    private fun makeSongCursor(context: Context): Cursor? {
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        return try {
            context.applicationContext.contentResolver.query(
                uri,
                baseProjection,
                null,
                null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER
            )
        } catch (e: SecurityException) {
            null
        }
    }
}