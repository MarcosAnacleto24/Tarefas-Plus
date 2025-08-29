package com.example.tarefas.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tarefas.R
import com.example.tarefas.data.model.Status
import com.example.tarefas.data.model.Task
import com.example.tarefas.databinding.FragmentDoneBinding
import com.example.tarefas.ui.adapter.TaskAdapter


class DoneFragment : Fragment() {

    private var _binding: FragmentDoneBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskAdapter: TaskAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentDoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView(getTasks())
    }

    private fun initRecyclerView(taskList: List<Task>) {
        taskAdapter = TaskAdapter(taskList)

        binding.rvTask.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTask.setHasFixedSize(true)
        binding.rvTask.adapter = taskAdapter
    }

    private fun getTasks() = listOf(
        Task("0", "Criar nova tela", Status.DONE),
        Task("1", "correr",Status.DONE),
        Task("2", "estudar de manhã",Status.DONE),
        Task("3", "jogar boa",Status.DONE),
        Task("4", "estudar a tarde",Status.DONE),
        Task("5", "estudar a tarde",Status.DONE)
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}