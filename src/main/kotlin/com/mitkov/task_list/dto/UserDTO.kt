package com.mitkov.task_list.dto

import com.mitkov.task_list.entities.Role
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "DTO for user information")
data class UserDTO(
    @Schema(description = "The unique ID of the user", example = "1")
    val id: Long,
    @Schema(description = "The username of the user", example = "john_doe")
    val username: String,
    @Schema(description = "The role assigned to the user", example = "ROLE_USER")
    val role: Role,
)
