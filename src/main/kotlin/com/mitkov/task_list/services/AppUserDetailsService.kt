package com.mitkov.task_list.services

import com.mitkov.task_list.entities.User
import com.mitkov.task_list.repositories.UserRepository
import com.mitkov.task_list.security.AppUserDetails
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AppUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user: User = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found!")
        return AppUserDetails(user)
    }

}
