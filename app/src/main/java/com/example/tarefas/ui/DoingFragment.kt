package com.example.tarefas.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.example.tarefas.R
import com.example.tarefas.data.model.Status
import com.example.tarefas.data.model.Task
import com.example.tarefas.databinding.FragmentDoingBinding
import com.example.tarefas.ui.adapter.TaskAdapter


class DoingFragment : Fragment() {

    private var _binding: FragmentDoingBinding? = null

    private val binding get() = _binding!!

    private lateinit var taskAdapter: TaskAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycleView(getTasks())
    }

    private fun initRecycleView(tasList: List<Task>) {
        taskAdapter = TaskAdapter(tasList)

        binding.rvTask.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTask.setHasFixedSize(true)
        binding.rvTask.adapter = taskAdapter
    }

    private fun getTasks() = listOf(
        Task("0", "Criar nova tela", Status.DOING),
        Task("1", "correr",Status.DOING),
        Task("2", "estudar de manhã",Status.DOING),
        Task("3", "jogar boa",Status.DOING),
        Task("4", "estudar a tarde",Status.DOING),
        Task("5", "estudar a tarde",Status.DOING)
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}