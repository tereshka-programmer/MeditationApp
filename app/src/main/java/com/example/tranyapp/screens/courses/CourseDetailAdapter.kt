package com.example.tranyapp.screens.courses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tranyapp.R
import com.example.tranyapp.databinding.ItemMeditationBinding
import com.example.tranyapp.model.meditations.entities.Meditation

interface ActionListener {
    fun navigateToRunnable(title: String)
}

class CourseDetailAdapter(
    private val listener: ActionListener
) : RecyclerView.Adapter<CourseDetailAdapter.CourseDetailViewHolder>(), View.OnClickListener {

    var listOfMeditations: List<Meditation> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseDetailViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMeditationBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return CourseDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseDetailViewHolder, position: Int) {
        val meditation = listOfMeditations[position]
        holder.itemView.tag = meditation

        with(holder.binding) {
            tvMeditation.text = meditation.title

            if (meditation.image.isNotBlank()) {
                Glide.with(imageMeditation.context)
                    .load(meditation.image)
                    .circleCrop()
                    .placeholder(R.drawable.forest)
                    .error(R.drawable.forest)
                    .into(imageMeditation)
            }
        }
    }

    override fun getItemCount(): Int = listOfMeditations.size

    class CourseDetailViewHolder(
        val binding: ItemMeditationBinding
        ) : RecyclerView.ViewHolder(binding.root)

    override fun onClick(view: View?) {
        val meditation = view?.tag as Meditation
        listener.navigateToRunnable(meditation.title)
    }
}