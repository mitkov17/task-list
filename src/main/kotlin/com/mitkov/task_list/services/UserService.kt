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
}
