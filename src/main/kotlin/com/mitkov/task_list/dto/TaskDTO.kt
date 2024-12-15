package com.mitkov.task_list.dto

import com.mitkov.task_list.entities.Priority
import com.mitkov.task_list.entities.Status
import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

@Schema(description = "DTO for task information")
data class TaskDTO(
    @Schema(
        description = "The unique ID of the task",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    val id: Long?,
    @Schema(description = "The title of the task", example = "Finish project documentation")
    val title: String,
    @Schema(
        description = "A detailed description of the task",
        example = "Complete the Swagger documentation for the project"
    )
    val description: String,
    @Schema(description = "The deadline for the task in ISO 8601 format", example = "2024-12-31T23:59:59")
    val deadline: Date,
    @Schema(description = "The priority level of the task", example = "HIGH")
    val priority: Priority,
    @Schema(description = "The current status of the task", example = "UNFINISHED")
    val status: Status
)
