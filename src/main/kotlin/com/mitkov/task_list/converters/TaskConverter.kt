package com.mitkov.task_list.converters

import com.mitkov.task_list.dto.TaskDTO
import com.mitkov.task_list.entities.Task
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Component

@Component
class TaskConverter {

    fun convertFromDTO(taskDTO: TaskDTO): Task {
        return Task(
            id = taskDTO.id,
            title = taskDTO.title,
            description = taskDTO.description,
            deadline = taskDTO.deadline,
            priority = taskDTO.priority,
            status = taskDTO.status,
            user = null
        )
    }

    fun convertToDTO(task: Task): TaskDTO {
        return TaskDTO(
            id = task.id,
            title = task.title,
            description = task.description,
            deadline = task.deadline,
            priority = task.priority,
            status = task.status
        )
    }
}
