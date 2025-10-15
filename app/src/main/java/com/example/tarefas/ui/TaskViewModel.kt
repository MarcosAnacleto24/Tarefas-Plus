package com.example.tarefas.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tarefas.data.model.Task
import com.example.tarefas.data.repository.TaskRepository
import com.example.tarefas.util.SingleLiveEvent
import com.example.tarefas.util.StateView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class TaskViewModel : ViewModel() {

    private val repository = TaskRepository()

    private val _taskList = MutableLiveData<StateView<List<Task>>>()
    val taskList: LiveData<StateView<List<Task>>> = _taskList

    private val _taskInsert = SingleLiveEvent<StateView<Task>>()
    val taskInsert: LiveData<StateView<Task>> = _taskInsert

    private val _taskUpdate = SingleLiveEvent<StateView<Task>>()
    val taskUpdate: LiveData<StateView<Task>> = _taskUpdate

    private val _taskDelete = SingleLiveEvent<StateView<Task>>()
    val taskDelete: LiveData<StateView<Task>> = _taskDelete

    private val database = FirebaseHelper.getDatabase()
        .child("tasks")
        .child(FirebaseHelper.getIdUser())

    private val valueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val taskList = mutableListOf<Task>()
            for (ds in snapshot.children) {
                ds.getValue(Task::class.java)?.let { task ->
                    taskList.add(task)
                }
            }
            // Ordena a lista pela data de atualização, da mais nova para a mais antiga
            _taskList.postValue(StateView.OnSuccess(taskList.sortedByDescending { it.updatedAt }))
        }

        override fun onCancelled(error: DatabaseError) {
            _taskList.postValue(StateView.OnError(error.message))
        }
    }

    fun getTasks() {
        _taskList.postValue(StateView.OnLoading())
        database.addValueEventListener(valueEventListener)
    }

    fun insertTask(task: Task) {
        _taskInsert.postValue(StateView.OnLoading())
        repository.saveTask(task) { result ->
            result.onSuccess { savedTask ->
                _taskInsert.postValue(StateView.OnSuccess(savedTask))
            }
            result.onFailure { exception ->
                _taskInsert.postValue(StateView.OnError(exception.message.toString()))
            }
        }
    }

    fun updateTask(task: Task) {
        _taskUpdate.postValue(StateView.OnLoading())
        repository.updateTask(task) { result ->
            result.onSuccess { updatedTask ->
                _taskUpdate.postValue(StateView.OnSuccess(updatedTask))
            }
            result.onFailure { exception ->
                _taskUpdate.postValue(StateView.OnError(exception.message.toString()))
            }
        }
    }

    fun moveTask(task: Task) {
        // Esta função apenas move a tarefa e não dispara um evento de UI (SingleLiveEvent)
        repository.moveTask(task) { result ->
            // Tratamento de erro pode ser adicionado aqui se necessário (ex: log)
        }
    }

    fun deleteTask(task: Task) {
        repository.deleteTask(task) { result ->
            result.onSuccess { deletedTask ->
                _taskDelete.postValue(StateView.OnSuccess(deletedTask))
            }
            result.onFailure { exception ->
                _taskDelete.postValue(StateView.OnError(exception.message.toString()))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        database.removeEventListener(valueEventListener)
    }
}