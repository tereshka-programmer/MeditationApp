package com.example.tranyapp.screens.songs

import android.content.Context
import android.media.MediaMetadataRetriever
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tranyapp.R
import com.example.tranyapp.databinding.ItemSongBinding
import com.example.tranyapp.model.songs.entities.Song
import com.example.tranyapp.screens.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface PlayerListener {

    fun onSongClick(position: Int)

}

class SongAdapter(
    val context: Context,
    private var songList: MutableList<Song>,
    private val playerListener: PlayerListener,
) : RecyclerView.Adapter<SongAdapter.ViewHolderSong>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderSong {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemSongBinding.inflate(layoutInflater, parent, false)
        return ViewHolderSong(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderSong, position: Int) {
        val song = songList[position]
        holder.itemView.tag = song

        holder.binding.root.setOnClickListener { playerListener.onSongClick(position) }

        with(holder.binding) {
            tvSongName.text = song.name
            tvSongArtist.text = song.artistName ?: "<Unknown>"
        }

        val activity = context as MainActivity
        activity.lifecycleScope.launch(Dispatchers.IO) {
            val imgByte = getSongThumbnail(song.path)
            withContext(Dispatchers.Main) {
                Glide.with(context).asBitmap().load(imgByte)
                    .placeholder(R.drawable.album)
                    .error(R.drawable.album)
                    .optionalCircleCrop()
                    .into(holder.binding.imageSong)
            }
        }
    }

    override fun getItemCount(): Int = songList.size

    fun updateSongList(songList: List<Song>) {
        this.songList = songList as MutableList<Song>
        notifyDataSetChanged()
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

    class ViewHolderSong(
        val binding: ItemSongBinding,
    ) : RecyclerView.ViewHolder(binding.root)


}