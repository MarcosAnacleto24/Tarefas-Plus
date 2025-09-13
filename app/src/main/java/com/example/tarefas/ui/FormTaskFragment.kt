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
import com.example.tarefas.util.initToolbar
import com.example.tarefas.util.showBottomSheet


class FormTaskFragment : BaseFragment() {

    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!
    private lateinit var task: Task
    private var newTask: Boolean = true

    private val args: FormTaskFragmentArgs by navArgs()

    private val viewModel: TaskViewModel by activityViewModels ()
    private var status: Status = Status.TODO


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
        getArgsConfigure()
        initListeners()
    }

    private fun getArgsConfigure() {
        args.task.let { it ->
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
            when(id) {
                R.id.rbTodo -> status = Status.TODO
                R.id.rbDoing -> status = Status.DOING
                else -> status = Status.DONE
            }
        }
    }

    private fun validateData() {
        val description = binding.editDescription.text.toString().trim()
        val details = binding.editDetails.text.toString().trim()
        
        if (description.isNotEmpty()) {

            hideKeyBoard()

            binding.progressBar.isVisible = true

            if (newTask) task = Task()
            task.description = description
            task.details = details
            task.status = status
            saveTask()
        } else {
            showBottomSheet(message = getString(R.string.txt_description))
        }
    }

    private fun saveTask() {
        FirebaseHelper.getDatabase()
            .child("tasks")
            .child(FirebaseHelper.getIdUser())
            .child(task.id)
            .setValue(task).addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    Toast.makeText(requireContext(), R.string.save_success, Toast.LENGTH_SHORT).show()

                    if (newTask) { //Criando nova Tarefa
                        findNavController().popBackStack()
                    } else { // Editando tarefa
                        viewModel.setUpdateTask(task)

                        binding.progressBar.isVisible = false
                    }

                } else {
                    binding.progressBar.isVisible = false
                    showBottomSheet(message = getString(R.string.error_save))
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}