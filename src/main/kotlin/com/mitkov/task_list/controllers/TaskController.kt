package com.mitkov.task_list.controllers

import com.mitkov.task_list.converters.TaskConverter
import com.mitkov.task_list.dto.TaskDTO
import com.mitkov.task_list.entities.Status
import com.mitkov.task_list.security.AppUserDetails
import com.mitkov.task_list.services.TaskService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tasks")
class TaskController(
    private val taskService: TaskService,
    private val taskConverter: TaskConverter
) {

    @Operation(
        summary = "Create a new task",
        description = "Allows a user or admin to create a new task associated with their account.",
        tags = ["Tasks"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Task created successfully",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = TaskDTO::class))]
            ),
            ApiResponse(responseCode = "403", description = "Access denied")
        ]
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun createTask(
        @AuthenticationPrincipal appUserDetails: AppUserDetails,
        @RequestBody @Valid taskDTO: TaskDTO
    ): ResponseEntity<TaskDTO> {
        val user = appUserDetails.getUser()
        val task = taskConverter.convertFromDTO(taskDTO)
        task.user = user

        val createdTask = taskService.createTask(task)
        return ResponseEntity.ok(taskConverter.convertToDTO(createdTask))
    }

    @Operation(
        summary = "Retrieve tasks for a user",
        description = "Retrieves all tasks associated with the authenticated user. Optionally filters by task status.",
        tags = ["Tasks"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Tasks retrieved successfully",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = TaskDTO::class, type = "array")
                )]
            ),
            ApiResponse(responseCode = "403", description = "Access denied")
        ]
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun getTasksByUser(
        @AuthenticationPrincipal appUserDetails: AppUserDetails,
        @RequestParam(required = false) status: Status?
    ): ResponseEntity<List<TaskDTO>> {
        val user = appUserDetails.getUser()
        val tasks = if (status != null) {
            taskService.getTasksByStatusAndUser(user, status)
        } else {
            taskService.getTasksByUser(user)
        }
        val taskDTOList = tasks.map { taskConverter.convertToDTO(it) }
        return ResponseEntity.ok(taskDTOList)
    }

    @Operation(
        summary = "Update a task",
        description = "Allows a user or admin to update an existing task.",
        tags = ["Tasks"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Task updated successfully",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = TaskDTO::class))]
            ),
            ApiResponse(responseCode = "404", description = "Task not found"),
            ApiResponse(responseCode = "403", description = "Access denied")
        ]
    )
    @PatchMapping("/{taskId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun updateTask(
        @AuthenticationPrincipal appUserDetails: AppUserDetails,
        @PathVariable taskId: Long,
        @RequestBody @Valid taskDTO: TaskDTO
    ): ResponseEntity<TaskDTO> {
        val user = appUserDetails.getUser()
        val updatedTask = taskService.updateTaskForUser(taskId, user, taskDTO)
        return ResponseEntity.ok(taskConverter.convertToDTO(updatedTask))
    }

    @Operation(
        summary = "Delete a task",
        description = "Allows a user or admin to delete a task by ID.",
        tags = ["Tasks"]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Task deleted successfully"),
            ApiResponse(responseCode = "404", description = "Task not found"),
            ApiResponse(responseCode = "403", description = "Access denied")
        ]
    )
    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun deleteTask(
        @AuthenticationPrincipal appUserDetails: AppUserDetails,
        @PathVariable taskId: Long
    ): ResponseEntity<Void> {
        val user = appUserDetails.getUser()
        taskService.deleteTaskById(taskId, user)
        return ResponseEntity.ok().build()
    }

    @Operation(
        summary = "Mark a task as completed",
        description = "Marks an existing task as completed.",
        tags = ["Tasks"]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Task marked as completed"),
            ApiResponse(responseCode = "404", description = "Task not found"),
            ApiResponse(responseCode = "403", description = "Access denied")
        ]
    )
    @PatchMapping("/{taskId}/complete")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun markTaskAsCompleted(
        @AuthenticationPrincipal appUserDetails: AppUserDetails,
        @PathVariable taskId: Long
    ): ResponseEntity<String> {
        val user = appUserDetails.getUser()
        taskService.markTaskAsCompleted(taskId, user)
        return ResponseEntity.ok("Task marked as completed")
    }

    @Operation(
        summary = "Retrieve all tasks",
        description = "Retrieves all tasks in the system (admin only).",
        tags = ["Tasks"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Tasks retrieved successfully",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = TaskDTO::class, type = "array")
                )]
            ),
            ApiResponse(responseCode = "403", description = "Access denied")
        ]
    )
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    fun getAllTasks(): ResponseEntity<List<TaskDTO>> {
        val tasks = taskService.getAllTasks()
        return ResponseEntity.ok(tasks.map { taskConverter.convertToDTO(it) })
    }

    @Operation(
        summary = "Update a task as admin",
        description = "Allows an admin to update any task in the system.",
        tags = ["Tasks"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Task updated successfully",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = TaskDTO::class))]
            ),
            ApiResponse(responseCode = "404", description = "Task not found"),
            ApiResponse(responseCode = "403", description = "Access denied")
        ]
    )
    @PatchMapping("/{taskId}/admin")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateTaskAsAdmin(
        @PathVariable taskId: Long,
        @RequestBody @Valid taskDTO: TaskDTO
    ): ResponseEntity<TaskDTO> {
        val updatedTask = taskService.updateTaskAsAdmin(taskId, taskDTO)
        return ResponseEntity.ok(taskConverter.convertToDTO(updatedTask))
    }

    @Operation(
        summary = "Delete a task as admin",
        description = "Allows an admin to delete any task in the system.",
        tags = ["Tasks"]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Task deleted successfully"),
            ApiResponse(responseCode = "404", description = "Task not found"),
            ApiResponse(responseCode = "403", description = "Access denied")
        ]
    )
    @DeleteMapping("/{taskId}/admin")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteTask(@PathVariable taskId: Long): ResponseEntity<Void> {
        taskService.deleteTaskById(taskId)
        return ResponseEntity.ok().build()
    }

}
