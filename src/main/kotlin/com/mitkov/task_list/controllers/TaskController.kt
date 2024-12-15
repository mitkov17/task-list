package com.mitkov.task_list.controllers

import com.mitkov.task_list.converters.TaskConverter
import com.mitkov.task_list.dto.TaskDTO
import com.mitkov.task_list.entities.Status
import com.mitkov.task_list.security.AppUserDetails
import com.mitkov.task_list.services.TaskService
import io.swagger.v3.oas.annotations.Operation
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

    @Operation(description = "Method that registers new sensors")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Success|OK"
        ), ApiResponse(responseCode = "409", description = "Sensor already exists!")]
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

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    fun getAllTasks(): ResponseEntity<List<TaskDTO>> {
        val tasks = taskService.getAllTasks()
        return ResponseEntity.ok(tasks.map { taskConverter.convertToDTO(it) })
    }

    @PatchMapping("/{taskId}/admin")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateTaskAsAdmin(
        @PathVariable taskId: Long,
        @RequestBody @Valid taskDTO: TaskDTO
    ): ResponseEntity<TaskDTO> {
        val updatedTask = taskService.updateTaskAsAdmin(taskId, taskDTO)
        return ResponseEntity.ok(taskConverter.convertToDTO(updatedTask))
    }

    @DeleteMapping("/{taskId}/admin")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteTask(@PathVariable taskId: Long): ResponseEntity<Void> {
        taskService.deleteTaskById(taskId)
        return ResponseEntity.ok().build()
    }

}
