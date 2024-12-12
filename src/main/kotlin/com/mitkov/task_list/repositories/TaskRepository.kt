package com.mitkov.task_list.repositories

import com.mitkov.task_list.entities.Task
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : JpaRepository<Task, Long> {
}
