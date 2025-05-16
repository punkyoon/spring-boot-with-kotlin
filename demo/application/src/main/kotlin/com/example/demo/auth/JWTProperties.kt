package com.example.demo.auth

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "auth.jwt")
data class JWTProperties(
    val issuer: String,
    val audience: String,
    val secretKey: String,
    val expiration: TokenExpirationConfig,
) {
    data class TokenExpirationConfig(
        val accessToken: Duration,
        val refreshToken: Duration,
    )
}