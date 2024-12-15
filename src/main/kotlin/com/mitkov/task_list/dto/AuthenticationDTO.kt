package com.mitkov.task_list.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "DTO for user authentication")
data class AuthenticationDTO(
    @Schema(description = "The username of the user", example = "john_doe")
    val username: String,
    @Schema(description = "The password of the user", example = "password123")
    val password: String
)
