package com.mitkov.task_list.dto

import com.mitkov.task_list.entities.Priority
import com.mitkov.task_list.entities.Status
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.*

data class TaskDTO(
    val id: Long?,
    @NotBlank(message = "Title cannot be empty")
    @Size(min = 3, max = 100, message = "Title length must be between 3 and 100 characters")
    val title: String,
    @NotBlank(message = "Description cannot be empty")
    val description: String,
    val deadline: Date,
    val priority: Priority,
    val status: Status
)
