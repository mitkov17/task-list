package com.mitkov.task_list.controllers

import com.mitkov.task_list.exceptions.*
import io.swagger.v3.oas.annotations.Hidden
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@Hidden
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(ex: UserNotFoundException): ResponseEntity<String> =
        ResponseEntity(ex.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(TaskNotFoundException::class)
    fun handleTaskNotFoundException(ex: TaskNotFoundException): ResponseEntity<String> =
        ResponseEntity(ex.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(UnauthorizedActionException::class)
    fun handleUnauthorizedActionException(ex: UnauthorizedActionException): ResponseEntity<String> =
        ResponseEntity(ex.message, HttpStatus.FORBIDDEN)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<String> =
        ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)

    @ExceptionHandler(TaskIsAlreadyCompletedException::class)
    fun handleTaskIsAlreadyCompletedException(ex: TaskIsAlreadyCompletedException): ResponseEntity<String> =
        ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)

    @ExceptionHandler(UserAlreadyExistException::class)
    fun handleUserAlreadyExistException(ex: UserAlreadyExistException): ResponseEntity<String> =
        ResponseEntity(ex.message, HttpStatus.CONFLICT)

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(ex: AccessDeniedException): ResponseEntity<Map<String, String>> {
        val response = mapOf(
            "error" to "Forbidden",
            "message" to "You do not have permission to access this resource."
        )
        return ResponseEntity(response, HttpStatus.FORBIDDEN)
    }
}
