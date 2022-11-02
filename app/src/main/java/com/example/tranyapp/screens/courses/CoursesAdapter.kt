package com.example.tranyapp.screens.courses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tranyapp.R
import com.example.tranyapp.databinding.ItemCourseBinding
import com.example.tranyapp.model.courses.entities.Course

class CoursesAdapter(

) : RecyclerView.Adapter<CoursesAdapter.CoursesAdapterViewHolder>() {

    var listOfCourses: List<Course> = emptyList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoursesAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCourseBinding.inflate(inflater, parent, false)

        return CoursesAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoursesAdapterViewHolder, position: Int) {
        val course = listOfCourses[position]

        with(holder.binding) {
            holder.itemView.tag = course

            titleCourse.text = course.title
            if (course.image.isNotBlank()) {
                Glide.with(imageCourse.context)
                    .load(course.image)
                    .placeholder(R.drawable.forest)
                    .error(R.drawable.forest)
                    .into(imageCourse)
            }
        }
    }

    override fun getItemCount(): Int = listOfCourses.size

    class CoursesAdapterViewHolder(
        val binding: ItemCourseBinding
    ) : RecyclerView.ViewHolder(binding.root)

}