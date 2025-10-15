package com.example.tarefas.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tarefas.R
import com.example.tarefas.data.model.Status
import com.example.tarefas.data.model.Task
import com.example.tarefas.databinding.FragmentFormTaskBinding
import com.example.tarefas.util.StateView
import com.example.tarefas.util.initToolbar
import com.example.tarefas.util.showBottomSheet

class FormTaskFragment : BaseFragment() {

    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var task: Task
    private var newTask: Boolean = true

    private val args: FormTaskFragmentArgs by navArgs()

    private val viewModel: TaskViewModel by activityViewModels()
    private var status: Status = Status.TODO


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentFormTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar)
        getArgsConfigure()
        initListeners()
        observeViewModel()
    }

    private fun getArgsConfigure() {
        args.task.let { 
            if (it != null) {
                this.task = it

                binding.editDescription.setText(task.description)
                binding.editDetails.setText(task.details)
                binding.titleToolbar.setText(R.string.edit_task)

                val id = when(task.status) {
                    Status.TODO -> R.id.rbTodo
                    Status.DOING -> R.id.rbDoing
                    else -> R.id.rbDone
                }

                binding.rbStatus.check(id)

                newTask = false
                status = task.status
            }
        }
    }
    
    private fun initListeners() {
        binding.btnSave.setOnClickListener {
            validateData()
        }

        binding.rbStatus.setOnCheckedChangeListener { _, id ->
            status = when(id) {
                R.id.rbTodo -> Status.TODO
                R.id.rbDoing -> Status.DOING
                else -> Status.DONE
            }
        }
    }

    private fun validateData() {
        val description = binding.editDescription.text.toString().trim()
        val details = binding.editDetails.text.toString().trim()
        
        if (description.isNotEmpty()) {
            hideKeyBoard()

            if (newTask) task = Task()
            task.description = description
            task.details = details
            task.status = status

            if (newTask) {
                viewModel.insertTask(task)
            } else {
                viewModel.updateTask(task)
            }
        } else {
            showBottomSheet(message = getString(R.string.txt_description))
        }
    }

    private fun observeViewModel() {
        viewModel.taskInsert.observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.OnLoading -> {
                    binding.progressBar.isVisible = true
                }
                is StateView.OnSuccess -> {
                    Toast.makeText(requireContext(), R.string.save_success, Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                is StateView.OnError -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(requireContext(), stateView.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.taskUpdate.observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.OnLoading -> {
                    binding.progressBar.isVisible = true
                }
                is StateView.OnSuccess -> {
                    Toast.makeText(requireContext(), R.string.update_task, Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                is StateView.OnError -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(requireContext(), stateView.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
