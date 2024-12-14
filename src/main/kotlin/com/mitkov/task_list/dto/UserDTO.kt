package com.mitkov.task_list.dto

import com.mitkov.task_list.entities.Role
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UserDTO(
    val id: Long,
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 100, message = "Username length must be between 3 and 100 characters")
    val username: String,

    val role: Role,
)
