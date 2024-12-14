package com.mitkov.task_list.controllers

import com.mitkov.task_list.converters.TaskConverter
import com.mitkov.task_list.dto.TaskDTO
import com.mitkov.task_list.entities.Task
import com.mitkov.task_list.entities.User
import com.mitkov.task_list.security.AppUserDetails
import com.mitkov.task_list.services.TaskService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tasks")
class TaskController (
    private val taskService: TaskService,
    private val taskConverter: TaskConverter
) {

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    fun createTask(@AuthenticationPrincipal appUserDetails: AppUserDetails,
                   @RequestBody @Valid taskDTO: TaskDTO) : ResponseEntity<TaskDTO> {
        val user = appUserDetails.getUser()
        val task = taskConverter.convertFromDTO(taskDTO)
        task.user = user

        val createdTask = taskService.createTask(task)
        return ResponseEntity.ok(taskConverter.convertToDTO(createdTask))
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    fun getTasksByUser(@AuthenticationPrincipal appUserDetails: AppUserDetails,
                       @RequestParam(required = false) status: String?) : ResponseEntity<List<TaskDTO>> {
        val user = appUserDetails.getUser()
        val tasks = if (status != null) {
            taskService.getTasksByStatusAndUser(user, status)
        } else {
            taskService.getTasksByUser(user)
        }
        val taskDTOList = tasks.map {taskConverter.convertToDTO(it)}
        return ResponseEntity.ok(taskDTOList)
    }

    @PatchMapping("/{taskId}")
    @PreAuthorize("hasRole('USER')")
    fun updateTask(
        @AuthenticationPrincipal appUserDetails: AppUserDetails,
        @PathVariable taskId: Long,
        @RequestBody @Valid taskDTO: TaskDTO
    ): ResponseEntity<TaskDTO> {
        val user = appUserDetails.getUser()
        val updatedTask = taskService.updateTaskForUser(taskId, user, taskDTO)
        return ResponseEntity.ok(taskConverter.convertToDTO(updatedTask))
    }

//    @PatchMapping("/{taskId}")
//    @PreAuthorize("hasRole('USER')")
//    fun updateTask (
//        @AuthenticationPrincipal appUserDetails: AppUserDetails,
//        @PathVariable taskId: Long,
//        @RequestBody @Valid taskDTO: TaskDTO
//    ) : ResponseEntity<TaskDTO> {
//        val user = appUserDetails.getUser()
//        val task = taskService.getTasksByUser(user).find { it.id == taskId }
//            ?: throw RuntimeException("Task not found or not owned by user")
//        val updatedTask = taskConverter.convertFromDTO(taskDTO)
//        updatedTask.id = task.id
//        updatedTask.user = task.user
//
//        val savedTask = taskService.updateTask(updatedTask)
//        return ResponseEntity.ok(taskConverter.convertToDTO(savedTask))
//    }

    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasRole('USER')")
    fun deleteTask (
        @AuthenticationPrincipal appUserDetails: AppUserDetails,
        @PathVariable taskId: Long
    ) : ResponseEntity<Void> {
        val user = appUserDetails.getUser()
        taskService.deleteTaskById(taskId, user)
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/{taskId}/complete")
    @PreAuthorize("hasRole('USER')")
    fun markTaskAsDone(
        @AuthenticationPrincipal appUserDetails: AppUserDetails,
        @PathVariable taskId: Long
    ) : ResponseEntity<String> {
        val user = appUserDetails.getUser()
        taskService.markTaskAsDone(taskId, user)
        return ResponseEntity.ok("Task marked as done")
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

    @DeleteMapping("/tasks/{taskId}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteTask(@PathVariable taskId: Long): ResponseEntity<Void> {
        taskService.deleteTaskById(taskId)
        return ResponseEntity.ok().build()
    }

}
