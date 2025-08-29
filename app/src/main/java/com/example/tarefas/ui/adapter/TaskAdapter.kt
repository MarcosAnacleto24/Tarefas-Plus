package com.example.tarefas.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tarefas.data.model.Task
import com.example.tarefas.databinding.ItemTaskBinding

class TaskAdapter(
    private val taskList: List<Task>
) : RecyclerView.Adapter<TaskAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task = taskList[position]

        holder.binding.txtDescription.text = task.description
    }

    override fun getItemCount() = taskList.size

    inner class MyViewHolder(val binding: ItemTaskBinding)
        : RecyclerView.ViewHolder(binding.root)
}