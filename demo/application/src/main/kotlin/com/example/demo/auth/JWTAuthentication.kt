package com.example.demo.auth

data class JWTAuthentication(
    val accessToken: String,
    val refreshToken: String,
)
