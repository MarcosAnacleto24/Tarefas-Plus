package com.example.tarefas.ui

import android.os.Bundle
import android.renderscript.Sampler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tarefas.R
import com.example.tarefas.data.model.Status
import com.example.tarefas.data.model.Task
import com.example.tarefas.databinding.FragmentDoneBinding
import com.example.tarefas.ui.adapter.TaskAdapter
import com.example.tarefas.util.showBottomSheet
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


class DoneFragment : Fragment() {

    private var _binding: FragmentDoneBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskAdapter: TaskAdapter

    private lateinit var reference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private val viewModel: TaskViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentDoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        reference = Firebase.database.reference

        observeViewModel()
        initRecyclerView()
        getTasks()
    }

    private fun observeViewModel() {
        viewModel.taskUpdate.observe(viewLifecycleOwner) {updateTask ->
            if (updateTask.status == Status.DONE) {

                //Armazena a lista atual do adapter
                val oldList = taskAdapter.currentList

                //Gera uma nova lista a partir da lista antiga já com a tarefa atualizada
                val newList = oldList.toMutableList().apply {
                    find { it.id == updateTask.id }?.description = updateTask.description
                }

                //Armazena a posição da tarefa a ser atualizada na lista
                val position = newList.indexOfFirst { it.id == updateTask.id }

                //Envia a lista atualizada para o adapter
                taskAdapter.submitList(newList)

                //atualiza a tarefa pela posição do adapter
                taskAdapter.notifyItemChanged(position)
            }
        }
    }

    private fun initRecyclerView() {
        taskAdapter = TaskAdapter(requireContext()) {task, option ->
            optionSelect(task, option)
        }

        with(binding.rvTask) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = taskAdapter
        }

    }

    private fun optionSelect(task: Task, option: Int) {
        when(option) {

            TaskAdapter.SELECT_BACK -> {
                Toast.makeText(requireContext(), "Back ${task.description}", Toast.LENGTH_SHORT).show()
            }

            TaskAdapter.SELECT_REMOVER -> {
                showBottomSheet(
                    titleDialog = R.string.title_task_delete_info,
                    titleButton = R.string.title_button,
                    message = getString(R.string.title_task_delete_confirm),
                    onClick = {deleteTask(task)}
                )
            }

            TaskAdapter.SELECT_EDIT -> {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToFormTaskFragment(task)
                findNavController().navigate(action)
            }

            TaskAdapter.SELECT_DETAILS -> {
                Toast.makeText(requireContext(), "Detalhes ${task.description}", Toast.LENGTH_SHORT).show()
            }



        }
    }

    private fun getTasks() {
        reference
            .child("tasks")
            .child(auth.currentUser?.uid ?: "")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val taskList = mutableListOf<Task>()

                    for(ds in snapshot.children) {
                        val task = ds.getValue(Task::class.java) as Task

                        if (task.status == Status.DONE) {
                            taskList.add(task)
                        }
                    }

                    listEmpty(taskList)

                    taskList.reverse()
                    taskAdapter.submitList(taskList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), R.string.error_save, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun listEmpty(taskList: List<Task>) {
        binding.progressBar.isVisible = false
        binding.txtInfo.text = if (taskList.isEmpty()) {
            getString(R.string.txt_list_task_empty)
        } else {
            ""
        }
    }

    private fun deleteTask(task: Task) {
        reference
            .child("tasks")
            .child(auth.currentUser?.uid ?: "")
            .child(task.id)
            .removeValue().addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    Toast.makeText(requireContext(), R.string.task_delete_success, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), R.string.error_save, Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}