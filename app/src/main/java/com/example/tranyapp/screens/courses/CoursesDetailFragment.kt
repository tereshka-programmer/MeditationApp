package com.example.tranyapp.screens.courses

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tranyapp.App
import com.example.tranyapp.R
import com.example.tranyapp.databinding.FragmentCoursesDetailBinding
import com.example.tranyapp.screens.TabsFragmentDirections
import com.example.tranyapp.utils.findTopNavController
import com.example.tranyapp.utils.viewModelCreator

class CoursesDetailFragment : Fragment(R.layout.fragment_courses_detail) {

    private lateinit var binding: FragmentCoursesDetailBinding
    private lateinit var adapter: CourseDetailAdapter

    private val viewModel by viewModelCreator { CoursesDetailViewModel(getCoursesDetail(), App.coursesRepository) }

    private val args: CoursesDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCoursesDetailBinding.bind(view)

        adapter = CourseDetailAdapter(object : ActionListener {
            override fun navigateToRunnable(title: String) {
                navigateToMeditationRunnable(title)
            }
        })

        val layoutManager = LinearLayoutManager(requireContext())

        binding.rcvCoursesDetail.adapter = adapter
        binding.rcvCoursesDetail.layoutManager = layoutManager

        viewModel.course.observe(viewLifecycleOwner) {
            adapter.listOfMeditations = it.value
            Glide.with(binding.imageCourse)
                .load(it.image)
                .circleCrop()
                .placeholder(R.drawable.ic_courses)
                .error(R.drawable.ic_courses)
                .into(binding.imageCourse)

            binding.tvTitleOfCourse.text = it.title
            binding.tvDescriptionOfCourse.text = it.description
        }

    }

    private fun navigateToMeditationRunnable(title: String) {
        val direction = TabsFragmentDirections.actionTabsFragmentToMeditationRunnableFragment(title)
        findTopNavController().navigate(direction)
    }

    private fun getCoursesDetail(): String = args.title
}