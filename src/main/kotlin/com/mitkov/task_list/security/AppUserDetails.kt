package com.mitkov.task_list.security

import com.mitkov.task_list.entities.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class AppUserDetails(private val user: User) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        mutableListOf(SimpleGrantedAuthority(user.role.name))

    override fun getPassword(): String = user.password

    override fun getUsername(): String = user.username

    fun getUser(): User = user
}
