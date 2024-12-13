package com.mitkov.task_list.services

import com.mitkov.task_list.entities.Status
import com.mitkov.task_list.entities.Task
import com.mitkov.task_list.entities.User
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

    fun getTasksByStatusAndUser(user: User, status: String): List<Task> {
        val taskStatus = try {
            Status.valueOf(status.uppercase())
        } catch (e: IllegalArgumentException) {
            throw RuntimeException("Invalid Status: $status")
        }
        return taskRepository.findByUserAndStatus(user, taskStatus)
    }

    @Transactional
    fun updateTask(task: Task): Task {
        return taskRepository.save(task)
    }

    @Transactional
    fun deleteTaskById(taskId: Long, user: User) {
        val task = taskRepository.findById(taskId).orElseThrow { RuntimeException("Task not found!") }
        if (task.user != user) {
            throw RuntimeException("You can't delete tasks that don't belong to you")
        }
        taskRepository.delete(task)
    }

    @Transactional
    fun markTaskAsDone(taskId: Long, user: User) {
        val task = taskRepository.findById(taskId).orElseThrow { RuntimeException("Task not found!") }
        if (task.user != user) {
            throw RuntimeException("You can't update tasks that don't belong to you")
        }
        task.status = Status.DONE
        taskRepository.save(task)
    }
}
