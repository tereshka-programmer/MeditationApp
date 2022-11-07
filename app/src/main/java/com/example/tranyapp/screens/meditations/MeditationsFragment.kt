package com.example.tranyapp.screens.meditations

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tranyapp.App
import com.example.tranyapp.R
import com.example.tranyapp.databinding.FragmentMeditationsBinding
import com.example.tranyapp.screens.TabsFragmentDirections
import com.example.tranyapp.utils.findTopNavController
import com.example.tranyapp.utils.viewModelCreator

class MeditationsFragment : Fragment(R.layout.fragment_meditations) {

    private lateinit var binding: FragmentMeditationsBinding
    private lateinit var adapter: MeditationsAdapter

    private val viewModel by viewModelCreator { MeditationsViewModel(App.meditationsRepository) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMeditationsBinding.bind(view)

        adapter = MeditationsAdapter(object : ActionListener {
            override fun navigateToMeditation(title: String) {
                navigateToMeditationRunnable(title)
            }

        })

        val layoutManager = LinearLayoutManager(requireContext())
        binding.meditationsRecyclerView.layoutManager = layoutManager
        binding.meditationsRecyclerView.adapter = adapter

        viewModel.meditation.observe(viewLifecycleOwner) {
            adapter.listOfMeditations = it
        }

    }

    private fun navigateToMeditationRunnable(title: String) {
        val direction = TabsFragmentDirections.actionTabsFragmentToMeditationRunnableFragment(title)
        findTopNavController().navigate(direction)
    }
}