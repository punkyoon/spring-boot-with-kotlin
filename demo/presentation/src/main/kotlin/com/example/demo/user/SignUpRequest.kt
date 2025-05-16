package com.example.demo.user

data class SignUpRequest(
    val loginUsername: String,
    val loginPassword: String,
    val name: String,
    val email: String,
)
