package com.example.tranyapp.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.tranyapp.R
import com.example.tranyapp.databinding.FragmentTabsBinding

class TabsFragment : Fragment(R.layout.fragment_tabs) {

    private lateinit var binding: FragmentTabsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTabsBinding.bind(view)
    }

}