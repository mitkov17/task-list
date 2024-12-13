package com.mitkov.task_list.repositories

import com.mitkov.task_list.entities.Status
import com.mitkov.task_list.entities.Task
import com.mitkov.task_list.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : JpaRepository<Task, Long> {
    fun findByUser(user: User): List<Task>
    fun findByUserAndStatus(user: User, status: Status): List<Task>
}
