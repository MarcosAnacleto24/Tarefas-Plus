package com.example.tarefas.data.repository

import com.example.tarefas.data.model.Task
import com.example.tarefas.ui.FirebaseHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener

class TaskRepository {

    private val database = FirebaseHelper.getDatabase()
        .child("tasks")
        .child(FirebaseHelper.getIdUser())

    fun saveTask(task: Task, callback: (Result<Task>) -> Unit) {
        val newTaskId = database.push().key ?: ""
        task.id = newTaskId

        val taskMap = mapOf(
            "id" to task.id,
            "description" to task.description,
            "details" to task.details,
            "status" to task.status,
            "updatedAt" to ServerValue.TIMESTAMP
        )

        database.child(task.id).setValue(taskMap).addOnCompleteListener { result ->
            if (result.isSuccessful) {
                callback(Result.success(task))
            } else {
                callback(Result.failure(result.exception ?: Exception("Falha ao salvar tarefa.")))
            }
        }
    }

    fun updateTask(task: Task, callback: (Result<Task>) -> Unit) {
        val taskMap = mapOf(
            "id" to task.id,
            "description" to task.description,
            "details" to task.details,
            "status" to task.status,
            "updatedAt" to ServerValue.TIMESTAMP
        )
        database.child(task.id).updateChildren(taskMap).addOnCompleteListener { result ->
            if (result.isSuccessful) {
                callback(Result.success(task))
            } else {
                callback(Result.failure(result.exception ?: Exception("Falha em atualizar tarefa.")))
            }
        }
    }

    fun moveTask(task: Task, callback: (Result<Task>) -> Unit) {
        val taskMap = mapOf(
            "status" to task.status,
            "updatedAt" to ServerValue.TIMESTAMP
        )
        database.child(task.id).updateChildren(taskMap).addOnCompleteListener { result ->
            if (result.isSuccessful) {
                callback(Result.success(task))
            } else {
                callback(Result.failure(result.exception ?: Exception("Falha em mover tarefa.")))
            }
        }
    }

    fun deleteTask(task: Task, callback: (Result<Task>) -> Unit) {
        database.child(task.id).removeValue().addOnCompleteListener { result ->
            if (result.isSuccessful) {
                callback(Result.success(task))
            } else {
                callback(Result.failure(result.exception ?: Exception("Falha em deletar tarefa.")))
            }
        }
    }
}