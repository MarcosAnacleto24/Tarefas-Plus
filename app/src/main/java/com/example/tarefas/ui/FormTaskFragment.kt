package com.example.tarefas.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tarefas.R
import com.example.tarefas.databinding.FragmentFormTaskBinding
import com.example.tarefas.util.initToolbar
import com.example.tarefas.util.showBottomSheet


class FormTaskFragment : Fragment() {

    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding = FragmentFormTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar)
        initListeners()
    }
    
    private fun initListeners() {
        binding.btnSave.setOnClickListener { 
            validateData()
        }
    }

    private fun validateData() {
        val description = binding.editDescription.text.toString().trim()
        
        if (description.isNotEmpty()) {
            Toast.makeText(requireContext(), "Tudo certo", Toast.LENGTH_SHORT).show()
        } else {
            showBottomSheet(message = getString(R.string.txt_description))
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}