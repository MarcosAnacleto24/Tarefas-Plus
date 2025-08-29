package com.example.tarefas.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.tarefas.R
import com.example.tarefas.data.model.Status
import com.example.tarefas.data.model.Task
import com.example.tarefas.databinding.ItemTaskBinding

class TaskAdapter(
    private val context: Context,
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

        setIndicators(task, holder)
    }

    private fun setIndicators(task: Task, holder: MyViewHolder) {
        when(task.status) {
            Status.TODO -> {
                holder.binding.back.isVisible = false
            }

            Status.DOING -> {
               holder.binding.back.setColorFilter(ContextCompat.getColor(context, R.color.color_back))
            }

            Status.DONE -> {
            holder.binding.next.isVisible = false
        }

        }
    }

    override fun getItemCount() = taskList.size

    inner class MyViewHolder(val binding: ItemTaskBinding)
        : RecyclerView.ViewHolder(binding.root)
}