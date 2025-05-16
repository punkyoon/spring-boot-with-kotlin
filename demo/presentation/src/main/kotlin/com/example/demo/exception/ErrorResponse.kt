package com.example.demo.exception

data class ErrorResponse(
    val code: ErrorCode,
    val message: String,
)
