package com.example.tarefas.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    var id: String = "",
    var description: String = "",
    var status: Status = Status.TODO,
    var details: String = "",
    var updatedAt: Long = 0L
) : Parcelable
