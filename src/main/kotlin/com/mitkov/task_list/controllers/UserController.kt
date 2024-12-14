package com.mitkov.task_list.controllers

import com.mitkov.task_list.converters.UserConverter
import com.mitkov.task_list.dto.UserDTO
import com.mitkov.task_list.entities.Role
import com.mitkov.task_list.services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
    private val userConverter: UserConverter
) {

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun getAllUsers(): ResponseEntity<List<UserDTO>> {
        val users = userService.getAllUsers().map { userConverter.convertToDTO(it) }
        return ResponseEntity.ok(users)
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteUser(@PathVariable userId: Long): ResponseEntity<Void> {
        userService.deleteUserById(userId)
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateUserRole(@PathVariable userId: Long, @RequestParam role: Role): ResponseEntity<String> {
        userService.updateUserRole(userId, role)
        return ResponseEntity.ok("User role updated to $role")
    }

}
