package com.example.demo.exception

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(ex: RuntimeException): ResponseEntity<ErrorResponse> {
        val statusCode = when (ex) {
            is ValidationException -> HttpStatus.BAD_REQUEST
            is NotFoundException -> HttpStatus.NOT_FOUND
            else -> HttpStatus.BAD_REQUEST
        }

        val errorCode = when (ex) {
            is ValidationException -> ErrorCode.BAD_REQUEST
            is NotFoundException -> ErrorCode.NOT_FOUND
            else -> ErrorCode.BAD_REQUEST
        }

        val message = when (ex) {
            is DataIntegrityViolationException -> "Data integrity violated error"
            else -> ex.message ?: "Unknown error occurred"
        }

        return ResponseEntity.status(statusCode).body(
            ErrorResponse(
                code = errorCode,
                message = message,
            )
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ErrorResponse(
                code = ErrorCode.INTERNAL_SERVER_ERROR,
                message = "Internal server error occurred",
            )
        )
    }
}