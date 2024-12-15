package com.mitkov.task_list.config

import com.mitkov.task_list.security.JWTUtil
import com.mitkov.task_list.services.AppUserDetailsService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JWTFilter(
    private val jwtUtil: JWTUtil,
    private val userDetailsService: AppUserDetailsService
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        if (!authHeader.isNullOrBlank() && authHeader.startsWith("Bearer ")) {
            val jwt = authHeader.substring(7)
            try {
                val (username, role) = jwtUtil.validateTokenAndRetrieveClaims(jwt)
                val userDetails: UserDetails = userDetailsService.loadUserByUsername(username)
                val authToken =
                    UsernamePasswordAuthenticationToken(userDetails, null, listOf(SimpleGrantedAuthority(role)))
                SecurityContextHolder.getContext().authentication = authToken
            } catch (e: Exception) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token")
                return
            }
        }
        filterChain.doFilter(request, response)
    }

}
