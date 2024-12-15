package com.mitkov.task_list.controllers

import com.mitkov.task_list.converters.UserConverter
import com.mitkov.task_list.dto.AuthenticationDTO
import com.mitkov.task_list.dto.RegistrationDTO
import com.mitkov.task_list.entities.User
import com.mitkov.task_list.security.JWTUtil
import com.mitkov.task_list.services.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
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

    @Operation(
        summary = "Register a new user",
        description = "Registers a new user in the system.",
        tags = ["Auth"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Registration successful",
                content = [Content(mediaType = "application/json")]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid registration data",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @PostMapping("/registration")
    fun performRegistration(@RequestBody registrationDTO: RegistrationDTO): ResponseEntity<String> {
        val user: User = userConverter.convertFromRegistrationDTO(registrationDTO)
        userService.register(user)
        return ResponseEntity.ok("Registration successful")
    }

    @Operation(
        summary = "Authenticate a user",
        description = "Authenticates a user and returns a JWT token.",
        tags = ["Auth"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Authentication successful",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(example = "{'jwt-token': 'string'}")
                )]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Invalid credentials",
                content = [Content(mediaType = "application/json")]
            )
        ]
    )
    @PostMapping("/login")
    fun performLogin(@RequestBody authenticationDTO: AuthenticationDTO): ResponseEntity<Map<String, String>> {
        val authInputToken = UsernamePasswordAuthenticationToken(authenticationDTO.username, authenticationDTO.password)

        try {
            authenticationManager.authenticate(authInputToken)
        } catch (e: BadCredentialsException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(mapOf("message" to "Incorrect credentials!"))
        }

        val user = userService.findByUsername(authenticationDTO.username)

        val token = jwtUtil.generateToken(user.username, user.role)
        return ResponseEntity.ok(mapOf("jwt-token" to token))
    }
}
