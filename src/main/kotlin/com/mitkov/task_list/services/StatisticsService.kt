package com.mitkov.task_list.services

import com.mitkov.task_list.entities.Status
import com.mitkov.task_list.exceptions.UserNotFoundException
import com.mitkov.task_list.repositories.TaskRepository
import com.mitkov.task_list.repositories.UserRepository
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat

@Service
class StatisticsService(
    private val taskService: TaskService,
    private val userService: UserService,
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository
) {

    fun getTaskStatistics(): Map<String, Any> {
        val totalTasks = taskService.getTotalTasksCount()
        val completedTasks = taskService.getTasksByStatusCount(Status.COMPLETED)
        val unfinishedTasks = taskService.getTasksByStatusCount(Status.UNFINISHED)
        val userStatistics = userService.getUserTasksStatistics()

        return mapOf(
            "totalTasks" to totalTasks,
            "completedTasks" to completedTasks,
            "unfinishedTasks" to unfinishedTasks,
            "userStatistics" to userStatistics
        )
    }

    fun exportStatisticsToCSV(response: HttpServletResponse) {
        response.contentType = "text/csv"
        response.addHeader("Content-Disposition", "attachment; filename=\"task_list_statistics.csv\"")

        response.outputStream.use { outputStream ->
            outputStream.writer(Charsets.UTF_8).use { writer ->
                writer.write("Username;TasksCount;CompletedTasks;UnfinishedTasks\n")

                val userStats = userService.getUserTasksStatistics()
                userStats.forEach { (username, tasksCount) ->
                    val user = userRepository.findByUsername(username)
                        ?: throw UserNotFoundException("User with username '$username' not found")

                    val completedTasks = taskRepository.countByUserAndStatus(user, Status.COMPLETED)
                    val unfinishedTasks = taskRepository.countByUserAndStatus(user, Status.UNFINISHED)

                    writer.write("$username;$tasksCount;$completedTasks;$unfinishedTasks\n")
                }
                writer.flush()
            }
        }
    }

    fun exportAllTasksSortedByDeadlineToCSV(response: HttpServletResponse) {
        response.contentType = "text/csv"
        response.addHeader("Content-Disposition", "attachment; filename=\"sorted_tasks.csv\"")

        response.outputStream.use { outputStream ->
            outputStream.writer(Charsets.UTF_8).use { writer ->
                writer.write("Title;Description;Deadline;Priority;Status;Username\n")

                val tasks = taskRepository.findAll().sortedByDescending { it.deadline }
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")

                tasks.forEach { task ->
                    val username = task.user?.username ?: "Unknown"
                    val title = task.title
                    val description = task.description
                    val deadline = dateFormat.format(task.deadline)
                    val priority = task.priority.name
                    val status = task.status.name

                    writer.write("$title;$description;$deadline;$priority;$status;$username\n")
                }
                writer.flush()
            }
        }
    }
}
