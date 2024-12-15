package com.mitkov.task_list.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "DTO for user registration")
data class RegistrationDTO(
    @Schema(description = "The username of the new user", example = "john_doe")
    val username: String,
    @Schema(description = "The password of the new user", example = "password123")
    val password: String
)
