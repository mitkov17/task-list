package com.mitkov.task_list.services

import com.mitkov.task_list.dto.TaskDTO
import com.mitkov.task_list.entities.Status
import com.mitkov.task_list.entities.Task
import com.mitkov.task_list.entities.User
import com.mitkov.task_list.exceptions.TaskIsAlreadyCompletedException
import com.mitkov.task_list.exceptions.TaskNotFoundException
import com.mitkov.task_list.exceptions.UnauthorizedActionException
import com.mitkov.task_list.repositories.TaskRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class TaskService(
    private val taskRepository: TaskRepository
) {

    @Transactional
    fun createTask(task: Task): Task {
        return taskRepository.save(task)
    }

    fun getTasksByUser(user: User): List<Task> {
        return taskRepository.findByUser(user)
    }

    fun getTasksByStatusAndUser(user: User, status: Status): List<Task> {
        return taskRepository.findByUserAndStatus(user, status)
    }

    @Transactional
    fun updateTaskForUser(taskId: Long, user: User, taskDTO: TaskDTO): Task {
        val task = taskRepository.findById(taskId).orElseThrow {
            TaskNotFoundException("Task with ID $taskId not found")
        }

        if (task.user != user) {
            throw UnauthorizedActionException("You can't update tasks that don't belong to you")
        }

        task.title = taskDTO.title
        task.description = taskDTO.description
        task.deadline = taskDTO.deadline
        task.priority = taskDTO.priority
        task.status = taskDTO.status

        return taskRepository.save(task)
    }

    @Transactional
    fun deleteTaskById(taskId: Long, user: User) {
        val task =
            taskRepository.findById(taskId).orElseThrow { TaskNotFoundException("Task with ID $taskId not found") }
        if (task.user != user) {
            throw UnauthorizedActionException("You can't delete tasks that don't belong to you")
        }
        taskRepository.delete(task)
    }

    @Transactional
    fun markTaskAsCompleted(taskId: Long, user: User) {
        val task = taskRepository.findById(taskId).orElseThrow { TaskNotFoundException("Task with ID $taskId not found") }
        if (task.user != user) {
            throw UnauthorizedActionException("You can't delete tasks that don't belong to you")
        }
        if (task.status == Status.COMPLETED) {
            throw TaskIsAlreadyCompletedException("Task with ID $taskId is already completed")
        }
        task.status = Status.COMPLETED
        taskRepository.save(task)
    }

    fun getAllTasks(): List<Task> {
        return taskRepository.findAll()
    }

    @Transactional
    fun updateTaskAsAdmin(taskId: Long, taskDTO: TaskDTO): Task {
        val task = taskRepository.findById(taskId).orElseThrow { TaskNotFoundException("Task with ID $taskId not found") }

        task.title = taskDTO.title
        task.description = taskDTO.description
        task.deadline = taskDTO.deadline
        task.priority = taskDTO.priority
        task.status = taskDTO.status

        return taskRepository.save(task)
    }

    @Transactional
    fun deleteTaskById(taskId: Long) {
        val task = taskRepository.findById(taskId).orElseThrow { TaskNotFoundException("Task with ID $taskId not found") }
        taskRepository.delete(task)
    }

    fun getTotalTasksCount(): Long {
        return taskRepository.count()
    }

    fun getTasksByStatusCount(status: Status): Long {
        return taskRepository.countByStatus(status)
    }
}
