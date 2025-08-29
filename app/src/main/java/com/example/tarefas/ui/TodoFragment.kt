package com.example.tarefas.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tarefas.R
import com.example.tarefas.data.model.Status
import com.example.tarefas.data.model.Task
import com.example.tarefas.databinding.FragmentTodoBinding
import com.example.tarefas.ui.adapter.TaskAdapter


class TodoFragment : Fragment() {

    private var _binding: FragmentTodoBinding? = null

    private val binding get() = _binding!!

    private lateinit var taskAdapter: TaskAdapter


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

        initRecyclerView(getTasks())
    }

    private fun initListeners() {
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_formTaskFragment)
        }
    }

    private fun initRecyclerView(taskList: List<Task> ) {

        taskAdapter = TaskAdapter(taskList)

        binding.rvTask.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTask.setHasFixedSize(true)
        binding.rvTask.adapter = taskAdapter
    }

    private fun getTasks() = listOf(
        Task("0", "Criar nova tela", Status.TODO),
        Task("1", "correr",Status.TODO),
        Task("2", "estudar de manhã",Status.TODO),
        Task("3", "jogar boa",Status.TODO),
        Task("4", "estudar a tarde",Status.TODO),
        Task("5", "estudar a tarde",Status.TODO)
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}