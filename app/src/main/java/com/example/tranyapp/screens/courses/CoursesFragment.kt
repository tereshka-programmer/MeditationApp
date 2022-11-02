package com.example.tranyapp.screens.courses

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tranyapp.App
import com.example.tranyapp.R
import com.example.tranyapp.databinding.FragmentCoursesBinding
import com.example.tranyapp.model.courses.InMemoryCoursesRepository
import com.example.tranyapp.screens.MainActivity
import com.example.tranyapp.utils.viewModelCreator

class CoursesFragment : Fragment(R.layout.fragment_courses) {

    private lateinit var binding: FragmentCoursesBinding
    private lateinit var adapter: CoursesAdapter



    private val viewModel by viewModelCreator { CoursesViewModel(App.coursesRepository) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCoursesBinding.bind(view)

        adapter = CoursesAdapter()
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.coursesRecyclerView.adapter = adapter
        binding.coursesRecyclerView.layoutManager = layoutManager

        viewModel.course.observe(viewLifecycleOwner) {
            adapter.listOfCourses = it
        }
    }

}