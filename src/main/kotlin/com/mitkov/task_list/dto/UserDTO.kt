package com.mitkov.task_list.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UserDTO(
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 100, message = "Username length must be between 3 and 100 characters")
    val username: String,

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 4, max = 100, message = "Password length must be between 4 and 100 characters")
    val password: String
)
