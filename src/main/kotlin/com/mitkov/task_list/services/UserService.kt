package com.mitkov.task_list.services

import com.mitkov.task_list.entities.Role
import com.mitkov.task_list.entities.User
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
        user.password = passwordEncoder.encode(user.password)
        if (user.role == null) {
            user.role = Role.ROLE_USER
        }
        return userRepository.save(user)
    }

    fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    @Transactional
    fun deleteUserById(userId: Long) {
        val user = userRepository.findById(userId).orElseThrow { RuntimeException("User not found!") }
        userRepository.delete(user)
    }

    @Transactional
    fun updateUserRole(userId: Long, role: Role) {
        val user = userRepository.findById(userId).orElseThrow {RuntimeException("User not found!")}
        user.role = role
        userRepository.save(user)
    }

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
