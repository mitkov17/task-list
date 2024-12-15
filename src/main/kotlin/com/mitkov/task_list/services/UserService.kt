package com.mitkov.task_list.services

import com.mitkov.task_list.entities.Role
import com.mitkov.task_list.entities.User
import com.mitkov.task_list.exceptions.UserAlreadyExistException
import com.mitkov.task_list.exceptions.UserNotFoundException
import com.mitkov.task_list.repositories.UserRepository
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun register(user: User): User {
        val existingUser = userRepository.findByUsername(user.username)
        if (existingUser != null) {
            throw UserAlreadyExistException("User with username '${user.username}' already exists")
        }
        user.password = passwordEncoder.encode(user.password)
        return userRepository.save(user)
    }

    fun findByUsername(username: String): User {
        return userRepository.findByUsername(username)
            ?: throw UserNotFoundException("User with username '$username' not found")
    }

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    @Transactional
    fun deleteUserById(userId: Long) {
        val user =
            userRepository.findById(userId).orElseThrow { UserNotFoundException("User with ID $userId not found") }
        userRepository.delete(user)
    }

    @Transactional
    fun updateUserRole(userId: Long, role: Role) {
        val user =
            userRepository.findById(userId).orElseThrow { UserNotFoundException("User with ID $userId not found") }
        user.role = role
        userRepository.save(user)
    }

    @Transactional
    fun createDefaultAdminIfNotExists() {
        val existingAdmin = userRepository.findByUsername("admin")
        if (existingAdmin == null) {
            val admin = User(
                username = "admin",
                password = passwordEncoder.encode("admin"),
                role = Role.ROLE_ADMIN
            )
            userRepository.save(admin)
        }
    }

    fun getUserTasksStatistics(): Map<String, Long> {
        return userRepository.findAll().associate { user ->
            user.username to user.tasks.size.toLong()
        }
    }
}
