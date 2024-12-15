package com.mitkov.task_list.controllers

import com.mitkov.task_list.converters.UserConverter
import com.mitkov.task_list.dto.UserDTO
import com.mitkov.task_list.entities.Role
import com.mitkov.task_list.services.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
    private val userConverter: UserConverter
) {

    @Operation(
        summary = "Get all users",
        description = "Retrieves a list of all registered users.",
        tags = ["Users"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "List of users retrieved successfully",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = UserDTO::class, type = "array")
                )]
            ),
            ApiResponse(responseCode = "403", description = "Access denied")
        ]
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun getAllUsers(): ResponseEntity<List<UserDTO>> {
        val users = userService.getAllUsers().map { userConverter.convertToDTO(it) }
        return ResponseEntity.ok(users)
    }

    @Operation(
        summary = "Delete a user",
        description = "Deletes a user by their ID.",
        tags = ["Users"]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User deleted successfully"),
            ApiResponse(responseCode = "404", description = "User not found"),
            ApiResponse(responseCode = "403", description = "Access denied")
        ]
    )
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteUser(@PathVariable userId: Long): ResponseEntity<Void> {
        userService.deleteUserById(userId)
        return ResponseEntity.ok().build()
    }

    @Operation(
        summary = "Update user role",
        description = "Updates the role of a user by their ID.",
        tags = ["Users"]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User role updated successfully"),
            ApiResponse(responseCode = "404", description = "User not found"),
            ApiResponse(responseCode = "403", description = "Access denied")
        ]
    )
    @PatchMapping("/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateUserRole(@PathVariable userId: Long, @RequestParam role: Role): ResponseEntity<String> {
        userService.updateUserRole(userId, role)
        return ResponseEntity.ok("User role updated to $role")
    }

}
