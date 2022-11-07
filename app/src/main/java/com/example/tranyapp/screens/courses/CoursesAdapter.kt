package com.example.tranyapp.screens.courses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tranyapp.R
import com.example.tranyapp.databinding.ItemCourseBinding
import com.example.tranyapp.model.courses.entities.Course

interface userActionListener {
    fun toNavigate(titile: String)
}

class CoursesAdapter(
    private val actionListener: userActionListener
) : RecyclerView.Adapter<CoursesAdapter.CoursesAdapterViewHolder>(), View.OnClickListener {

    var listOfCourses: List<Course> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoursesAdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCourseBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return CoursesAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoursesAdapterViewHolder, position: Int) {
        val course = listOfCourses[position]
        holder.itemView.tag = course
        with(holder.binding) {


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

    override fun onClick(view: View?) {
        val course = view?.tag as Course
        actionListener.toNavigate(course.title)
    }

}