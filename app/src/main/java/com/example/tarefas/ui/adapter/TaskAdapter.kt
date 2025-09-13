package com.example.tarefas.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tarefas.R
import com.example.tarefas.data.model.Status
import com.example.tarefas.data.model.Task
import com.example.tarefas.databinding.ItemTaskBinding

class TaskAdapter(
    private val context: Context,
    private val taskSelector: (Task, Int) -> Unit
) : ListAdapter<Task, TaskAdapter.MyViewHolder>(DIFF_CALLBACK) {

    companion object {
        val SELECT_BACK: Int = 1
        val SELECT_REMOVER: Int = 2
        val SELECT_EDIT: Int = 3
        val SELECT_INFORMATION: Int = 4
        val SELECT_NEXT: Int = 5


        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(
                oldItem: Task,
                newItem: Task
            ): Boolean {
                return oldItem.id == newItem.id && oldItem.description == newItem.description
            }

            override fun areContentsTheSame(
                oldItem: Task,
                newItem: Task
            ): Boolean {
                return oldItem == newItem && oldItem.description == newItem.description
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task = getItem(position)

        holder.binding.txtDescription.text = task.description

        setIndicators(task, holder)

        holder.binding.btnDelete.setOnClickListener { taskSelector(task, SELECT_REMOVER) }
        holder.binding.btnEdit.setOnClickListener { taskSelector(task,SELECT_EDIT) }
        holder.binding.btnDetails.setOnClickListener { taskSelector(task,SELECT_INFORMATION) }
    }

    private fun setIndicators(task: Task, holder: MyViewHolder) {
        when(task.status) {
            Status.TODO -> {
                holder.binding.back.isVisible = false

                holder.binding.next.setOnClickListener { taskSelector(task,SELECT_NEXT) }
            }

            Status.DOING -> {
               holder.binding.back.setColorFilter(ContextCompat.getColor(context, R.color.color_back))

                holder.binding.next.setOnClickListener { taskSelector(task,SELECT_NEXT) }
                holder.binding.back.setOnClickListener { taskSelector(task,SELECT_BACK) }
            }

            Status.DONE -> {
                holder.binding.next.isVisible = false

                holder.binding.back.setOnClickListener { taskSelector(task,SELECT_BACK) }
        }

        }
    }

    inner class MyViewHolder(val binding: ItemTaskBinding)
        : RecyclerView.ViewHolder(binding.root)
}