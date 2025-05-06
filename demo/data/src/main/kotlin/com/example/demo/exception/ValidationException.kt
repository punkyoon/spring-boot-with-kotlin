package com.example.demo.exception

class ValidationException(
    override val message: String,
    override val cause: Throwable? = null,
) : RuntimeException(message, cause)