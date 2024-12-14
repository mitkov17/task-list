package com.mitkov.task_list.controllers

import com.mitkov.task_list.converters.UserConverter
import com.mitkov.task_list.dto.AuthenticationDTO
import com.mitkov.task_list.dto.RegistrationDTO
import com.mitkov.task_list.entities.User
import com.mitkov.task_list.security.JWTUtil
import com.mitkov.task_list.services.UserService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val userService: UserService,
    private val jwtUtil: JWTUtil,
    private val authenticationManager: AuthenticationManager,
    private val userConverter: UserConverter
) {

    @PostMapping("/registration")
    fun performRegistration(@RequestBody @Valid registrationDTO: RegistrationDTO): ResponseEntity<String> {
        val user: User = userConverter.convertFromRegistrationDTO(registrationDTO)
        userService.register(user)
        return ResponseEntity.ok("Registration successful")
    }

    @PostMapping("/login")
    fun performLogin(@RequestBody authenticationDTO: AuthenticationDTO): Map<String, String> {
        val authInputToken = UsernamePasswordAuthenticationToken(authenticationDTO.username, authenticationDTO.password)

        try {
            authenticationManager.authenticate(authInputToken)
        } catch (e: BadCredentialsException) {
            return mapOf("message" to "Incorrect credentials!")
        }

        val user = userService.findByUsername(authenticationDTO.username)
            ?: throw RuntimeException("User not found")

        val token = jwtUtil.generateToken(user.username, user.role)
        return mapOf("jwt-token" to token)
    }
}
