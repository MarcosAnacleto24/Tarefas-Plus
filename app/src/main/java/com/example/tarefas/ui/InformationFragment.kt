package com.example.tarefas.ui

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.tarefas.R
import com.example.tarefas.data.model.Status
import com.example.tarefas.data.model.Task
import com.example.tarefas.databinding.FragmentInformationBinding
import com.example.tarefas.util.initToolbar


class InformationFragment : Fragment() {
    private var _binding: FragmentInformationBinding? = null
    private val binding get() = _binding!!
    private val args: InformationFragmentArgs by navArgs()

    private lateinit var task: Task

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInformationBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar)
        getArgsConfigure()
    }

    private fun getArgsConfigure() {
        this.task = args.task

        binding.txtDescription.text = getString(R.string.txt_get_description_information, task.description)

        when(task.status) {
            Status.TODO -> {
                binding.txtStatus.text = getString(R.string.txt_get_information_todo)
            }

            Status.DOING -> {
                binding.txtStatus.text = getString(R.string.txt_get_information_doing)
            }

            else -> {
                binding.txtStatus.text = getString(R.string.txt_get_information_done)
            }
        }

        if (task.details.isEmpty()) {

            binding.editInformation.setText(R.string.txt_details_information)

            binding.editInformation.gravity = Gravity.CENTER
        } else {
            binding.editInformation.setText(task.details)
            binding.editInformation.gravity = Gravity.TOP
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}