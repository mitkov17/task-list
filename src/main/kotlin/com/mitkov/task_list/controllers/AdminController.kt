package com.mitkov.task_list.controllers

import com.mitkov.task_list.converters.TaskConverter
import com.mitkov.task_list.dto.TaskDTO
import com.mitkov.task_list.entities.Role
import com.mitkov.task_list.entities.Status
import com.mitkov.task_list.entities.Task
import com.mitkov.task_list.entities.User
import com.mitkov.task_list.services.StatisticsService
import com.mitkov.task_list.services.TaskService
import com.mitkov.task_list.services.UserService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.core.io.FileSystemResource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
class AdminController (
    private val userService: UserService,
    private val taskService: TaskService,
    private val taskConverter: TaskConverter,
    private val statisticsService: StatisticsService
) {

    @GetMapping("/users")
    fun getAllUsers(): ResponseEntity<List<User>> {
        val users = userService.getAllUsers()
        return ResponseEntity.ok(users)
    }

    @DeleteMapping("/users/{userId}")
    fun deleteUser(@PathVariable userId: Long): ResponseEntity<Void> {
        userService.deleteUserById(userId)
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/users/{userId}/role")
    fun updateUserRole(
        @PathVariable userId: Long,
        @RequestParam role: Role
    ): ResponseEntity<String> {
        userService.updateUserRole(userId, role)
        return ResponseEntity.ok("User role update to role $role")
    }

    @GetMapping("/tasks")
    fun getAllTasks(): ResponseEntity<List<TaskDTO>> {
        val tasks = taskService.getAllTasks()
        val taskDTOList = tasks.map {taskConverter.convertToDTO(it)}
        return ResponseEntity.ok(taskDTOList)
    }

    @PatchMapping("/tasks/{taskId}")
    fun updateTaskAsAdmin(
        @PathVariable taskId: Long,
        @RequestBody @Valid taskDTO: TaskDTO
    ): ResponseEntity<TaskDTO> {
        val updatedTask = taskService.updateTaskAsAdmin(taskId, taskDTO)
        return ResponseEntity.ok(taskConverter.convertToDTO(updatedTask))
    }

    @DeleteMapping("/tasks/{taskId}")
    fun deleteTask(@PathVariable taskId: Long): ResponseEntity<Void> {
        taskService.deleteTaskById(taskId)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/statistics")
    fun getTaskStatistics(): ResponseEntity<Map<String, Any>> {
        val stats = statisticsService.getTaskStatistics()
        return ResponseEntity.ok(stats)
    }

    @GetMapping("/statistics/export")
    fun exportStatisticsToCSV(response: HttpServletResponse) {
        statisticsService.exportStatisticsToCSV(response)
    }

    @GetMapping("/statistics/export-sorted")
    fun exportSortedTasks(response: HttpServletResponse) {
        statisticsService.exportAllTasksSortedByDeadlineToCSV(response)
    }
}
