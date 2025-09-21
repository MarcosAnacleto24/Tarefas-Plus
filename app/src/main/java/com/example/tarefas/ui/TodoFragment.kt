package com.example.tarefas.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tarefas.R
import com.example.tarefas.data.model.Status
import com.example.tarefas.data.model.Task
import com.example.tarefas.databinding.FragmentTodoBinding
import com.example.tarefas.ui.adapter.TaskAdapter
import com.example.tarefas.util.showBottomSheet
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class TodoFragment : Fragment() {

    private var _binding: FragmentTodoBinding? = null

    private val binding get() = _binding!!

    private lateinit var taskAdapter: TaskAdapter

    private val viewModel: TaskViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()

        initRecyclerView()

        observeViewModel()

        viewModel.getTasks(Status.TODO)
    }

    private fun initListeners() {
        binding.fabAdd.setOnClickListener {
            val action = HomeFragmentDirections
                .actionHomeFragmentToFormTaskFragment(null)
            findNavController().navigate(action)
        }

    }

    private fun observeViewModel() {

        viewModel.taskList.observe(viewLifecycleOwner) { taskList ->
            listEmpty(taskList)

            taskAdapter.submitList(taskList)
        }

        viewModel.taskInsert.observe(viewLifecycleOwner) {task ->
            if (task.status == Status.TODO) {

                //Armazena a lista atual do adapter
                val oldList = taskAdapter.currentList

                //Gera uma nova lista a partir da lista antiga já com a tarefa atualizada
                val newList = oldList.toMutableList().apply {
                    add(0,task)
                }

                //Envia a lista atualizada para o adapter
                taskAdapter.submitList(newList)

                setPositionRecycleView()
            }
        }

        viewModel.taskUpdate.observe(viewLifecycleOwner) {updateTask ->
            //Armazena a lista atual do adapter
            val oldList = taskAdapter.currentList

            //Gera uma nova lista a partir da lista antiga já com a tarefa atualizada
            val newList = oldList.toMutableList().apply {
                if (updateTask.status == Status.TODO) {
                    find { it.id == updateTask.id }?.description = updateTask.description
                } else {
                    remove(updateTask)
                }
            }

            //Armazena a posição da tarefa a ser atualizada na lista
            val position = newList.indexOfFirst { it.id == updateTask.id }

            //Envia a lista atualizada para o adapter
            taskAdapter.submitList(newList)

            //atualiza a tarefa pela posição do adapter
            taskAdapter.notifyItemChanged(position)
        }
    }

    private fun initRecyclerView() {

        taskAdapter = TaskAdapter(requireContext()) { task, option ->
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

            TaskAdapter.SELECT_REMOVER -> {
                showBottomSheet(
                    titleDialog = R.string.title_task_delete_info,
                    titleButton = R.string.title_button,
                    message = getString(R.string.title_task_delete_confirm),
                    onClick = { deleteTask(task) }
                )
            }

            TaskAdapter.SELECT_EDIT -> {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToFormTaskFragment(task)
                findNavController().navigate(action)
            }

            TaskAdapter.SELECT_INFORMATION -> {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToInformationFragment(task)
                findNavController().navigate(action)
            }

            TaskAdapter.SELECT_NEXT -> {
                task.status = Status.DOING
                viewModel.updateTask(task)
            }

        }

    }



    private fun setPositionRecycleView() {
        taskAdapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
            override fun onChanged() {

            }
            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {

            }
            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {

            }
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.rvTask.scrollToPosition(0)
            }
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {

            }
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {

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
        FirebaseHelper.getDatabase()
            .child("tasks")
            .child(FirebaseHelper.getIdUser())
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